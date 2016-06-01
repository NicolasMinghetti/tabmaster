from django.conf.urls import url, include
from rest_framework.urlpatterns import format_suffix_patterns
from tabmaster.tabmaster import views

# API endpoints
urlpatterns = format_suffix_patterns([
    url(r'^$', views.api_root),
    url(r'^music/$',
        views.MusicList.as_view(),
        name='music-list'),
    url(r'^music/(?P<pk>[0-9]+)/$',
        views.MusicDetail.as_view(),
        name='music-detail'),
])

# Login and logout views for the browsable API
urlpatterns += [
    url(r'^api-auth/', include('rest_framework.urls',
                               namespace='rest_framework')),
]
