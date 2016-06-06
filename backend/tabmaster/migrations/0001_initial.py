# -*- coding: utf-8 -*-
# Generated by Django 1.9.6 on 2016-06-06 17:24
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Music',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False)),
                ('created', models.DateTimeField(auto_now_add=True)),
                ('title', models.CharField(max_length=100)),
                ('owner', models.CharField(max_length=100)),
                ('num_stars', models.DecimalField(blank=True, decimal_places=5, default='', max_digits=7)),
                ('num_stars_votes', models.IntegerField(blank=True, default='')),
                ('tablature', models.TextField()),
            ],
            options={
                'ordering': ('num_stars',),
            },
        ),
        migrations.CreateModel(
            name='TabRetrieve',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False)),
                ('data', models.CharField(max_length=1000)),
                ('tab', models.CharField(max_length=1000)),
            ],
            options={
                'ordering': ('id',),
            },
        ),
    ]
