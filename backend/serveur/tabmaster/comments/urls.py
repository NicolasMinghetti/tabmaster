from django.conf.urls import include, url
from . import views

urlpatterns = [
    url(r'^(?P<musicId>\d+)$', views.index, name='comments_details'),
]
