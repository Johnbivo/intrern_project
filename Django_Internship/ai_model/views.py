import os
import torch
import torch.nn as nn
import torch.nn.functional as F
from torchvision import transforms
from PIL import Image
import json
from django.http import HttpResponse, JsonResponse
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt



# Define the same model architecture used during training
class ClassifierModel(nn.Module):

    def __init__(self, num_classes=2):
        super().__init__()
        self.conv1 = nn.Conv2d(in_channels=3, out_channels=16, kernel_size=3, padding=1)
        self.conv2 = nn.Conv2d(in_channels=16, out_channels=32, kernel_size=3, padding=1)
        self.conv3 = nn.Conv2d(in_channels=32, out_channels=64, kernel_size=3, padding=1)
        self.pool = nn.MaxPool2d(kernel_size=2, stride=2)
        self.dropout = nn.Dropout(0.5)
        self.fc1 = nn.Linear(64 * 32 * 32, 256)
        self.fc2 = nn.Linear(256, 128)
        self.fc3 = nn.Linear(128, num_classes)

    def forward(self, x):
        x = self.pool(F.relu(self.conv1(x)))
        x = self.pool(F.relu(self.conv2(x)))
        x = self.pool(F.relu(self.conv3(x)))
        x = x.view(-1, 64 * 32 * 32)
        x = self.dropout(F.relu(self.fc1(x)))
        x = self.dropout(F.relu(self.fc2(x)))
        return self.fc3(x)
    




# Set device to GPU if available, otherwise use CPU
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# Make the ClassifierModel class available in the global namespace for torch.load
import sys
sys.modules["__main__"].ClassifierModel = ClassifierModel


# Load the model from the .pth file
model_path = os.path.join(os.path.dirname(__file__), "model.pth")
model = torch.load(model_path, map_location=device, weights_only=False)
model.eval()


# Define the image transformation pipeline the same as training
transform = transforms.Compose([
    transforms.Resize((256, 256)),
    transforms.ToTensor(),
    transforms.Normalize([0.5, 0.5, 0.5], [0.5, 0.5, 0.5]),
])

CLASSES = ["cat", "dog"]

# Had issues with the csrf while testing, so I exempted the predict function
@csrf_exempt
def predict(request):
    if request.method != "POST" or not request.FILES.get("image"):
        return JsonResponse({"error": "POST request with an image file required"}, status=400)

    image = Image.open(request.FILES["image"]).convert("RGB")
    input_tensor = transform(image).unsqueeze(0).to(device)

    with torch.no_grad():
        output = model(input_tensor)

    probabilities = torch.softmax(output, dim=1)
    confidence, predicted_idx = probabilities.max(dim=1)

    return JsonResponse({

        "predicted_class": CLASSES[predicted_idx.item()],
        "confidence": round(confidence.item(), 4),

    })



def ai_classifer(request):
    prediction = None
    percentage = None
    if request.method == "POST" and request.FILES.get("image"):
        image = Image.open(request.FILES["image"]).convert("RGB")
        input_tensor = transform(image).unsqueeze(0).to(device)

        with torch.no_grad():
            output = model(input_tensor)

        probabilities = torch.softmax(output, dim=1)
        confidence, predicted_idx = probabilities.max(dim=1)
        prediction = CLASSES[predicted_idx.item()]
        percentage = round(confidence.item() * 100, 2)

    return render(request, "ai_model/classify.html", {"prediction": prediction, "percentage": percentage})

