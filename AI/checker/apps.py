"""from django.apps import AppConfig


class CheckerConfig(AppConfig):
    default_auto_field = "django.db.models.BigAutoField"
    name = "checker" """
from django.apps import AppConfig
import html
from pathlib import Path
import os
from models import similarity_Checker

class WebappConfig(AppConfig):
    MODEL_PATH = Path("model")
    predictor = similarity_Checker()