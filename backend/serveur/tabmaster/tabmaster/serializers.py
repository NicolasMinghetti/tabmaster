from rest_framework import serializers
from tabmaster.tabmaster.models import Music
from tabmaster.tabmaster.models import TabRetrieve

class MusicSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Music
        fields = ('id', 'created', 'title', 'owner', 'num_stars', 'num_stars_votes', 'tablature')

class TabRetrieveSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = TabRetrieve
        fields = ('id', 'data', 'tab')
