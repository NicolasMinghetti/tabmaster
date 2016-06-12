from django.shortcuts import render
from django.http import HttpResponse


# Create your views here.
def index(request,musicId):
    return render(request, 'comments/facebookComments.html', {'url': musicId})
