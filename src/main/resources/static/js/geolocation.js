function GeoLocator() {
  return {
    getLocation: function() {
      var deferred = $.Deferred();

      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
          deferred.resolve(position);
        }, deferred.reject);
      } else {
        deferred.reject("Your browser does not support geolocation");
      }

      return deferred.promise();
    }
  }
}
