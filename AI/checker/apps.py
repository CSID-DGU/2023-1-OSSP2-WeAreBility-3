"""from django.apps import AppConfig


class CheckerConfig(AppConfig):
    default_auto_field = "django.db.models.BigAutoField"
    name = "checker" """
from django.apps import AppConfig
import html
import pathlib
import os
import walking_path_similarity_execution

class WebappConfig(AppConfig):
    name = 'checker'
    predictor = walking_path_similarity_execution.similarity_Checker()