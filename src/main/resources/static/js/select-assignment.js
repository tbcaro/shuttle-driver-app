function SelectAssignmentApp() {
  var self = this;
  var intervalId;
  var geoLocator = null;
  var shuttleActivity = null;
  var assignmentCursor = 0;
  var driverAssignments = [];

  var elements = { };

  self.initialize = function() {
    // TBC : Setup elements
    elements.btnRefresh = $('#btn-refresh');
    elements.assignmentCard = $('#assignment-card');
    elements.routeNameContainer = $('#route-name-container');
    elements.scheduleCard = $('#schedule-card');
    elements.btnPrev = $('#btn-prev');
    elements.btnNext = $('#btn-next');
    elements.btnBeginAssignment = $('#btn-begin-assignment');

    elements.btnPrev.prop('disabled', true);
    elements.btnNext.prop('disabled', true);
    elements.btnBeginAssignment.prop('disabled', true);

    geoLocator = new GeoLocator();
    shuttleActivity = new ShuttleActivity();

    fetchAllAssignments();
    intervalId = setInterval(postActivity, 5000);
    bindEventHandlers();
  };

  var bindEventHandlers = function() {
    elements.btnRefresh.on('click', function() {
      window.location.reload(true);
    });

    elements.btnPrev.on('click', function() {

    });

    elements.btnNext.on('click', function() {

    });

    elements.btnBeginAssignment.on('click', function() {

    });
  };

  var postActivity = function() {
    geoLocator.getLocation().done(function(position){
      shuttleActivity.latitude = position.coords.latitude;
      shuttleActivity.longitude = position.coords.longitude;
      shuttleActivity.heading = position.coords.heading || 0;
      shuttleActivity.status = 'ACTIVE';

      axios.post('/api/postActivity', shuttleActivity)
          .then(function(response) {
            console.log(response);
          })
          .catch(function(error) {
            console.log(error);
          });
    });
  };

  var fetchAllAssignments = function() {
    axios.get('/api/fetchAllAssignments')
        .then(function(response) {
          console.log(response);
        })
        .catch(function(error) {
          console.log(error);
        });
  };

  self.initialize();
  return self;
}
