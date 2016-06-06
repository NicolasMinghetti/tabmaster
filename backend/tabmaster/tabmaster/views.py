from rest_framework import viewsets
from tabmaster.tabmaster.serializers import MusicSerializer
from tabmaster.tabmaster.serializers import TabRetrieveSerializer
from tabmaster.tabmaster.models import Music
from tabmaster.tabmaster.models import TabRetrieve
from rest_framework import generics
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework.reverse import reverse


@api_view(['GET'])
def api_root(request, format=None):
    return Response({
        'music': reverse('music-list', request=request, format=format),
        'tab-retrieve': reverse('tab-retrieve-list', request=request, format=format),
    })

class MusicList(generics.ListCreateAPIView):
    serializer_class = MusicSerializer

    def get_queryset(self):
        """
        Optionally restricts the returned music to a given user,
        by filtering against a `owner` query parameter in the URL.
        """
        queryset = Music.objects.all()
        owner = self.request.query_params.get('owner', None)
        if owner is not None:
            queryset = queryset.filter(owner=owner)
        return queryset


class MusicDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = Music.objects.all()
    serializer_class = MusicSerializer

class TabRetrieveList(generics.ListCreateAPIView):
    serializer_class = TabRetrieveSerializer
    queryset = TabRetrieve.objects.all()

class TabRetrieveDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = TabRetrieve.objects.all()
    serializer_class = TabRetrieveSerializer
