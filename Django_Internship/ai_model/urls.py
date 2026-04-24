from django.urls import path
from . import views

urlpatterns = [
    path("", views.ai_classifer, name="classify"),
    path("predict/", views.predict, name="predict"),
]
