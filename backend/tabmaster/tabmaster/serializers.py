from rest_framework import serializers
from tabmaster.tabmaster.models import Music

class MusicSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Music
        fields = ('id', 'created', 'title', 'owner', 'num_stars', 'num_stars_votes', 'tablature')
