function SelectAssignmentApp() {
  var self = this;
  var intervalId;
  var geoLocator = null;
  var shuttleActivity = null;
  var assignmentCursor = 0;
  var driverAssignments = [];
  var timeUtils;
  var heading = 0;

  var elements = { };

  self.initialize = function() {
    // TBC : Setup elements
    elements.btnRefresh = $('#btn-refresh');
    elements.assignmentCard = $('#assignment-card');
    elements.routeNameContainer = $('#route-name-container');
    elements.scheduleCard = $('#schedule-card');
    elements.scheduleCardBody = elements.scheduleCard.find('tbody');
    elements.btnPrev = $('#btn-prev');
    elements.btnNext = $('#btn-next');
    elements.btnBeginAssignment = $('#btn-begin-assignment');
    elements.beginAssignmentForm = $('#begin-assignment-form');

    elements.btnPrev.prop('disabled', true);
    elements.btnNext.prop('disabled', true);
    elements.btnBeginAssignment.prop('disabled', true);

    geoLocator = new GeoLocator();
    shuttleActivity = new ShuttleActivity();
    timeUtils = new TimeUtils();

    fetchAllAssignments();
    intervalId = setInterval(postActivity, 5000);
    bindEventHandlers();
  };

  var bindEventHandlers = function() {
    elements.btnRefresh.on('click', function() {
      window.location.reload(true);
    });

    elements.btnPrev.on('click', function() {
      assignmentCursor--;
      checkPageControlsEnabled();
      bindSelectedAssignmentData();
    });

    elements.btnNext.on('click', function() {
      assignmentCursor++;
      checkPageControlsEnabled();
      bindSelectedAssignmentData();
    });

    elements.btnBeginAssignment.on('click', function() {
      try {
        var assignment = driverAssignments[assignmentCursor];
        var form = elements.beginAssignmentForm;
        form.append(getInput('assignmentId', assignment.assignmentReport.assignmentId));
        form.submit();
      } catch (ex) {
        alert('something went wrong: \n\n' + ex.message);
      }
    });

    Compass.watch(function (_heading) {
      heading = _heading;
    });
  };

  var postActivity = function() {
    geoLocator.getLocation().done(function(position){
      shuttleActivity.latitude = position.coords.latitude;
      shuttleActivity.longitude = position.coords.longitude;
      shuttleActivity.status = 'ACTIVE';
      shuttleActivity.heading = heading;

      $('#debug-lat').html('lat: ' + shuttleActivity.latitude);
      $('#debug-long').html('long: ' + shuttleActivity.longitude);
      $('#debug-heading').html('heading: ' + shuttleActivity.heading);

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
          driverAssignments = response.data;
          checkPageControlsEnabled();
          bindSelectedAssignmentData();
        })
        .catch(function(error) {
          console.log(error);
        });
  };

  var checkPageControlsEnabled = function() {
    if (driverAssignments.length === 0) {
      elements.btnPrev.prop('disabled', true);
      elements.btnNext.prop('disabled', true);
      elements.btnBeginAssignment.prop('disabled', true);
    } else {
      (assignmentCursor === 0) ? elements.btnPrev.prop('disabled', true) : elements.btnPrev.prop('disabled', false);
      (assignmentCursor === driverAssignments.length - 1) ? elements.btnNext.prop('disabled', true) : elements.btnNext.prop('disabled', false);

      try {
        if (driverAssignments[assignmentCursor] == null) {
          elements.btnBeginAssignment.prop('disabled', true);
        } else {
          elements.btnBeginAssignment.prop('disabled', false);
        }
      } catch (ex) {
        elements.btnBeginAssignment.prop('disabled', true);
      }
    }
  };

  var bindSelectedAssignmentData = function() {
    if (driverAssignments.length > 0) {
      var assignment = driverAssignments[assignmentCursor];

      elements.routeNameContainer.html(assignment.routeName);
      elements.scheduleCardBody.empty();

      var report = assignment.assignmentReport;
      if (report != null && report.assignmentStops != null) {
        report.assignmentStops.forEach(function (stop) {
          var row = $('<tr>');
          var iconCol = $('<td>').append($('<i>').addClass('fa').addClass('fa-map-pin'));
          var stopNameCol = $('<td>');
          var arriveTimeCol = $('<td>');

          stopNameCol.html(stop.name);
          arriveTimeCol.html(timeUtils.formatTime(stop.estArriveTime));

          row.append(iconCol);
          row.append(stopNameCol);
          row.append(arriveTimeCol);

          elements.scheduleCardBody.append(row);
        });
      }
    }
  };

  var getInput = function(name, value) {
    var input = $('<input>');
    input.prop('type', 'hidden');
    input.prop('name', name);
    input.val(value);

    return input;
  };

  self.initialize();
  return self;
}
