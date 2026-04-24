#!/bin/bash

pip install Django Pillow

if command -v nvidia-smi &> /dev/null; then
    echo "GPU detected, installing CUDA build..."
    pip install torch torchvision --index-url https://download.pytorch.org/whl/cu124
else
    echo "No GPU detected, installing CPU build..."
    pip install torch torchvision
fi
