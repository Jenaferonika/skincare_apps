from flask import Flask, request, jsonify
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing.text import Tokenizer
from sklearn.preprocessing import LabelEncoder
import pytesseract
from PIL import Image
import pandas as pd
import os
import io

# Konfigurasi Flask
app = Flask(__name__)

# Konfigurasi Tesseract
pytesseract.pytesseract.tesseract_cmd = r'/usr/bin/tesseract'

# Load model
model = load_model('skincare_category_model_final.h5', compile=False)

# Load dataset
datasetAll_df = pd.read_csv('dataset_allnew.csv')

# Daftar kategori berdasarkan benefits
category_rules = {
    "For Dry Skin": ["hydrating", "soothing"],
    "For Mature Skin": ["dark spot fading", "anti-aging"],
    "For Sensitive Skin": ["soothing"],
    "For Normal Skin/Combinational Skin": ["hydrating"],
    "For Oily Skin": ["oil control"]
}

# Endpoint untuk memproses gambar
@app.route('/process-image', methods=['POST'])
def process_image():
    # Periksa apakah file gambar dikirim
    if 'image' not in request.files:
        return jsonify({"error": "No image file provided"}), 400
    
    image_file = request.files['image']
    if image_file.filename == '':
        return jsonify({"error": "No selected image"}), 400
    
    # Buka gambar
    img = Image.open(io.BytesIO(image_file.read()))
    
    # Ekstrak teks menggunakan Tesseract
    detected_text = pytesseract.image_to_string(img, config="--psm 6")
    detected_tokens = detected_text.lower().split()

    # Tokenizer untuk ingredient
    ingredient_tokenizer = Tokenizer(num_words=1000, oov_token="<OOV>")
    ingredient_tokenizer.fit_on_texts(detected_tokens)
    matched_ingredients = [token for token in detected_tokens if token in ingredient_tokenizer.word_index]

    # LabelEncoder untuk rating
    le = LabelEncoder()
    ratings = ["BEST", "GOOD", "AVERAGE", "BAD", "WORST"]
    le.fit(ratings)

 # Proses data
    ingredient_functions = {}
    ingredient_ratings = {}
    benefit_matches = []

    for ingredient in matched_ingredients:
        matched_rows = datasetAll_df[datasetAll_df['ingredient_name'].str.contains(ingredient, case=False, na=F>
        if not matched_rows.empty:
            function_text = matched_rows['functions'].iloc[0]
            benefit_text = matched_rows['benefits'].iloc[0]
            ingredient_rating = matched_rows['rating'].iloc[0]

            ingredient_functions[ingredient] = function_text
            ingredient_ratings[ingredient] = ingredient_rating
            benefit_matches.append(str(benefit_text).lower())

    # Analisis kategori berdasarkan manfaat
    category_predictions = []
    for category, rules in category_rules.items():
        if all(rule in " ".join(benefit_matches) for rule in rules):
            category_predictions.append(category)

    # Buat output dalam format JSON
    output = {
        "ingredients": [],
        "predicted_skincare_categories": category_predictions

 }
    if ingredient_functions:
        for ingredient in ingredient_functions:
            output["ingredients"].append({
                "name": ingredient,
                "function": ingredient_functions[ingredient],
                "predicted_rating": ingredient_ratings.get(ingredient, "UNKNOWN")
            })

    return jsonify(output)

# Menjalankan server Flask
if __name__ == '__main__':
    app.run(debug=True)