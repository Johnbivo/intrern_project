# Cat vs Dog Image Classifier

A PyTorch image classifier trained on the [Microsoft Cats vs Dogs dataset](https://www.microsoft.com/en-us/download/details.aspx?id=54765).
Two models are included: a custom CNN built from scratch, and a ResNet18 transfer learning model.

---

## Project Structure

```
Work_project_AI/
├── archive/PetImages/     # Full dataset (Cat / Dog)
├── training/              # Sampled training images (cat / dog)
├── classification_model.ipynb   # Original notebook
├── model.pth              # Saved custom model
├── model1.pth             # Saved ResNet18 model
├── requirements.txt
└── README.md
```

-- Note that the dataset is not included in the repo. You can find the used dataset here: https://www.kaggle.com/datasets/bhavikjikadara/dog-and-cat-classification-dataset

---

## Setup

**1. Create and activate the virtual environment**

```bash
python -m venv venv
venv\Scripts\activate
```

**2. Install dependencies**

```bash
pip install -r requirements.txt --index-url https://download.pytorch.org/whl/cu126
```

> Use `cpu` instead of `cu126` if you don't have an NVIDIA GPU.

---

## Prepare Training Data

Run this once to sample 400 images per class from the full dataset into the `training/` folder:

```python
import os, random, shutil

def copy_random(src, dst, n=400):
    os.makedirs(dst, exist_ok=True)
    files = [f for f in os.listdir(src) if f.lower().endswith('.jpg')]
    for f in random.sample(files, n):
        shutil.copy2(os.path.join(src, f), os.path.join(dst, f))

random.seed(42)
copy_random("archive/PetImages/Cat", "training/cat", 400)
copy_random("archive/PetImages/Dog", "training/dog", 400)
```

---

## Running the Notebook

Open the ipynb file and ran only the `ìmports cell`, `model definition cell` and the `gradio cells` in VS Code or Jupyter and run cells top to bottom.

The notebook is split into two independent sections:

### Section 1 — Custom CNN

- Defines a 3-layer CNN 3 Fully Connected trained from scratch
- ~65-75% accuracy on 800 images
- 80.64% accuracy on 25k images

### Section 2 — ResNet18 (Transfer Learning)

- Uses pretrained ImageNet weights, fine-tuned for cat vs dog
- ~96.25%+ accuracy on the same 800 images

---

## Running the Gradio UI

Each section ends with a Gradio cell. Run it and open the local URL printed in the output (e.g. `http://127.0.0.1:7860`).

Upload any cat or dog image to get a prediction with confidence scores.

To stop all running Gradio interfaces:

```python
gr.close_all()
```

---

## Requirements

- Python 3.12
- NVIDIA GPU with CUDA 12.6+ (optional but recommended)
- See `requirements.txt` for package versions
