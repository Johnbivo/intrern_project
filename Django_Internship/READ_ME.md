# Django Internship — Cat vs Dog Image Classifier

A Django web application that uses a pre-trained PyTorch CNN model to classify uploaded images as either a **cat** or a **dog**, returning the predicted class and confidence percentage.

---

## How to Run

### Prerequisites

Install the required dependencies using the provided script, which automatically detects whether you have an Nvidia GPU and installs the CUDA or CPU build of PyTorch accordingly:

```bash
bash install.sh
```

> **Windows troubleshooting:** If you get `WSL ERROR: execvpe(/bin/bash) failed: No such file or directory`, WSL has no Linux distro installed so `bash` doesn't exist. Either install one with `wsl --install -d Ubuntu` and reopen your terminal, or skip the script and install manually (see below).

Alternatively, to install manually:

```bash
pip install -r requirements.txt
```

### Steps

```bash
# Activate the virtual environment (Windows)
venv\Scripts\activate.bat

# Start the development server
python manage.py runserver
```

The app will be available at `http://127.0.0.1:8000/`.

---

## Endpoints

| Method | URL          | View                          | Description                                                              |
| ------ | ------------ | ----------------------------- | ------------------------------------------------------------------------ |
| GET    | `/`          | `ai_model.views.ai_classifer` | Renders the upload form                                                  |
| POST   | `/`          | `ai_model.views.ai_classifer` | Accepts an uploaded image, runs inference, and renders result in browser |
| POST   | `/predict/`  | `ai_model.views.predict`      | JSON API — accepts an image file, returns predicted class + confidence   |

---

## URL & View Breakdown

### Root URL config — `Django_Internship/urls.py`

```python
path("", include("ai_model.urls"))
```

All routes are delegated to the `ai_model` app.

### App URL config — `ai_model/urls.py`

```python
path("", views.ai_classifer, name="classify")
path("predict/", views.predict, name="predict")
```

### View — `ai_model/views.py` → `ai_classifer(request)`

Handles **GET** and **POST**. On POST, reads the uploaded image, preprocesses it (resize to 256×256, normalize), runs it through the CNN, and renders the template with:
  - `prediction` — `"cat"` or `"dog"`
  - `percentage` — confidence score as a float (e.g. `97.43`)

### View — `ai_model/views.py` → `predict(request)`

JSON API endpoint (`@csrf_exempt`). Accepts a **POST** request with an `image` file field and returns:

```json
{
  "predicted_class": "dog",
  "confidence": 0.9743
}
```

Example usage with curl:
```bash
curl -X POST http://127.0.0.1:8000/predict/ -F "image=@photo.jpg"
```

---

## Project Structure

```
Django_Internship/
├── manage.py
├── db.sqlite3
├── Django_Internship/
│   ├── settings.py
│   └── urls.py
└── ai_model/
    ├── views.py          # Single view handling GET + POST
    ├── urls.py           # App URL patterns
    ├── model.pth         # Pre-trained CNN weights (~67 MB)
    ├── resnet_model.pth  # Alternative model weights (~67 MB)
    └── templates/
        └── ai_model/
            └── classify.html
```

---

## Model Details

The CNN is a custom PyTorch architecture trained to distinguish cats from dogs:

- Three convolutional blocks (3→16→32→64 channels) each followed by max-pooling
- Two fully-connected layers (64×32×32 → 256 → 128) with 0.5 dropout
- Output layer: 2 neurons — index 0 = `cat`, index 1 = `dog`
- Automatically uses CUDA if available, otherwise falls back to CPU
