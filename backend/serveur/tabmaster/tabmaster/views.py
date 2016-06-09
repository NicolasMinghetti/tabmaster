from rest_framework import viewsets
from tabmaster.tabmaster.serializers import MusicSerializer
from tabmaster.tabmaster.serializers import TabRetrieveSerializer
from tabmaster.tabmaster.models import Music
from tabmaster.tabmaster.models import TabRetrieve
from rest_framework import generics
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework.reverse import reverse
from django.http import HttpResponse, HttpResponseNotFound
import datetime
import matlab.engine


@api_view(['GET'])
def api_root(request, format=None):
    return Response({
        'music': reverse('music-list', request=request, format=format),
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

# A lire https://docs.google.com/drawings/d/1oED4UF5qQqCEVgZ0Nis8xNQLS69x4HH378rAnILXBYg/edit
# pour voir comment marche les échanges de données

def GenerateTab(request):

	print(datetime.datetime.now().time());
	data=request.body; #récup échantillon


	s=""
	tab = list();
	index = 0;
	for cara in data :

		if cara != 44:
			s += chr(int(cara));

		if cara == 44:
			tab.append(float(s));
			index = index +1;
			s="";

	print(datetime.datetime.now().time());
	eng = matlab.engine.connect_matlab(); #connection a l'engine matlab
	print(datetime.datetime.now().time());
	frame = matlab.double(tab);


	framen = eng.normalizer(frame);
	resf = eng.traitement(framen, 1);

	print(datetime.datetime.now().time());
	return HttpResponse(resf);


def test(request, format=None):

	return HttpResponse("voili voilou")
