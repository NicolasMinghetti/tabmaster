from django.db import models

# Create your models here.

class Music(models.Model):
    id = models.AutoField(primary_key=True)
    created = models.DateTimeField(auto_now_add=True)
    player = models.CharField(max_length=100)
    title = models.CharField(max_length=100)
    owner = models.CharField(max_length=100)
    num_stars = models.FloatField(blank=True, default='')
    num_stars_votes = models.IntegerField(blank=True, default='')

    tablature = models.TextField()


    def save(self, *args, **kwargs):
        if(self.id is not None):
            if(self.num_stars != ''):
                old_stars=Music.objects.get(id=self.id).num_stars
                old_stars_votes=Music.objects.get(id=self.id).num_stars_votes

                self.num_stars = ((old_stars)*old_stars_votes + self.num_stars)/(old_stars_votes+1)
                self.num_stars_votes = old_stars_votes+1
        super(Music, self).save(*args, **kwargs) # Call the "real" save() method.

    class Meta:
        ordering = ('num_stars',)


class TabRetrieve(models.Model):
    id = models.AutoField(primary_key=True)
    data = models.CharField(max_length=1000)
    tab = models.CharField(max_length=1000)
    class Meta:
        ordering = ('id',)
