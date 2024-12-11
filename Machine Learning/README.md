# Lifecycle of Machine-Learning Project

# Skincare Ingredients Scanner
A system that scans skincare product ingredients to provide users with detailed information, including functions, ratings, and benefits of each ingredient. The system helps users make informed decisions about skincare products.

## 1. Project Planning and Setup
* **Goals**: Build a machine learning-based application to analyze and classify skincare ingredients, providing information about their functions, ratings, and benefits.
* **Users**: Skincare enthusiasts, dermatologists, and general consumers.
* **Evaluation**: Measure model performance through metrics such as accuracy and loss. Visualize training/validation metrics and provide a prediction function to classify and explain ingredient information from user input.

## 2. Data Collection and Labeling
* **Data availability and collection**:
  * **Ingredients dataset**:
    * A dataset of ingredients, including names, functions, ratings (e.g., BEST, GOOD, AVERAGE, BAD, WORST), and benefits, collected from trusted sources like PubChem, INCI, or skincare databases.
  * **Product dataset**:
    * A dataset containing detailed product information used to create a backend database for skincare products.

* **Data preprocessing**:
  * Convert all text to lowercase:
    ```python
    df['ingredient_name'] = df['ingredient_name'].apply(lambda x: x.lower())
    ```
  * Remove unwanted characters and punctuation:
    ```python
    df['ingredient_name'] = df['ingredient_name'].apply(lambda x: re.sub(r'[^a-zA-Z0-9\s]', '', x))
    ```
  * Tokenize and preprocess ingredient data:
    ```python
    df['Tokens'] = df['ingredient_name'].apply(word_tokenize)
    ```
  * Combine significant phrases for better context:
    ```python
    def combine_important_words(tokens):
        combined_tokens = []
        skip_next = False
        for i in range(len(tokens) - 1):
            if skip_next:
                skip_next = False
                continue
            if tokens[i] in ['anti', 'pro', 'non', 'free', 'ultra', 'super']:
                combined_tokens.append(tokens[i] + '_' + tokens[i+1])
                skip_next = True
            else:
                combined_tokens.append(tokens[i])
        if not skip_next:
            combined_tokens.append(tokens[-1])
        return combined_tokens
    
    df['Combined_Tokens'] = df['Tokens'].apply(combine_important_words)
    ```
  * Remove stopwords:
    ```python
    stopwords_ind = stopwords.words('english')
    df['Clean_Tokens'] = df['Combined_Tokens'].apply(lambda x: [word for word in x if word.lower() not in stopwords_ind])

    df['clean_text'] = df['Clean_Tokens'].apply(lambda x: ' '.join(x))
    ```

## 3. Data Splitting
* Dataset contains 2400 rows of data, covering various skincare ingredients. The data is split into training (80%) and test (20%) sets for model development.

## 4. Model Training and Debugging
* **Model selection**: The system uses a supervised learning classification model to analyze ingredients and predict their functions, ratings, and benefits.
* **Model architecture**:
  * The model includes embedding layers for ingredient text analysis. The architecture uses the following layers:
    - `Dense(units=128, activation='relu')` layer
    - `Dense(units=64, activation='relu')` layer
    - `Dropout(0.3)` layer
    - `Dense(units=3, activation='softmax')` layer (to predict functions, ratings, and benefits)

* **Result**:
  - `Loss: 0.4%`
  - `Accuracy: 97%`
* **Debugging**: To handle overfitting, data augmentation and K-fold cross-validation were implemented. While high accuracy is achieved, some ingredients with ambiguous labels may result in misclassifications.

## 5. Deployment
* The trained model is deployed as a backend service on Google Cloud. The service allows users to upload product images, extract ingredient information using OCR (e.g., Tesseract), and display detailed ingredient analysis.

## Prerequisites
* Libraries used in this project:
  * `numpy==1.22.4`
  * `pandas==1.5.3`
  * `tensorflow==2.12.0`
  * `nltk==3.8.1`
  * `sklearn==1.2.2`
  * `pytesseract==0.3.10`
  * `Pillow==9.4.0`

