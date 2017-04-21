function OnAssignmentApp() {
  const DRIVING = 'DRIVING';
  const AT_STOP = 'AT_STOP';

  var self = this;
  var intervalId;
  var geoLocator = null;
  var shuttleActivity = null;
  var stopCursor = 0;
  var assignment = null;
  var timeUtils;

  var elements = { };

  self.initialize = function() {
    // TBC : Setup elements
    elements.btnEndAssignmentContainer = $('#btn-end-assignment-container');
    elements.btnFinishAssignmentContainer = $('#btn-finish-assignment-container');
    elements.labelStatus = $('#status-label');
    elements.labelStatusPreposition = $('#status-preposition');
    elements.labelStatusCurrentStop = $('#current-stop-status');
    elements.labelStatusStop = $('#status-stop');
    elements.assignmentCard = $('#assignment-card');
    elements.routeNameContainer = $('#route-name-container');
    elements.currentStopContainer = $('#current-stop-container');
    elements.stopArriveTime = $('#est-stop-arrive');
    elements.stopDepartTime = $('#est-stop-depart');
    elements.btnPrev = $('#btn-prev');
    elements.btnNext = $('#btn-next');
    elements.btnChangeStatus = $('#btn-change-status');

    elements.btnPrev.prop('disabled', true);
    elements.btnNext.prop('disabled', true);
    // elements.btnChangeStatus.prop('disabled', true);
    elements.btnFinishAssignmentContainer.hide();

    geoLocator = new GeoLocator();
    shuttleActivity = new ShuttleActivity();
    timeUtils = new TimeUtils();

    shuttleActivity.currentStopIndex = 0;
    shuttleActivity.status = DRIVING;

    axios.get('/api/fetchActiveAssignment')
        .then(function(response) {
          console.log(response);
          assignment = response.data;
          checkPageControlsEnabled();
          bindAssignmentData();
          bindCurrentStatus();
          bindBtnChangeStatusData();
        })
        .catch(function(error) {
          console.log(error);
        });
    intervalId = setInterval(postActivity, 5000);
    bindEventHandlers();
  };

  var bindEventHandlers = function() {
    elements.btnPrev.on('click', function() {
      stopCursor--;
      checkPageControlsEnabled();
      bindAssignmentData();
      bindCurrentStatus();
      bindBtnChangeStatusData();
    });

    elements.btnNext.on('click', function() {
      stopCursor++;
      checkPageControlsEnabled();
      bindAssignmentData();
      bindCurrentStatus();
      bindBtnChangeStatusData();
    });

    elements.btnChangeStatus.on('click', function() {
      var nextStopIndex = elements.btnChangeStatus.data('nextStopIndex');
      var nextStatus = elements.btnChangeStatus.data('nextStatus');

      if (nextStopIndex != null && nextStatus != null) {
        stopCursor = nextStopIndex;
        shuttleActivity.currentStopIndex = nextStopIndex;
        shuttleActivity.status = nextStatus;

        checkPageControlsEnabled();
        bindAssignmentData();
        bindCurrentStatus();
        bindBtnChangeStatusData();
      }
    });
  };

  var postActivity = function() {
    geoLocator.getLocation().done(function(position){
      shuttleActivity.latitude = position.coords.latitude;
      shuttleActivity.longitude = position.coords.longitude;
      (position.coords.heading == null || isNaN(position.coords.heading)) ? shuttleActivity.heading = position.coords.heading : shuttleActivity.heading = 0;

      axios.post('/api/postActivity', shuttleActivity)
          .then(function(response) {
            alert(
                "lat: " + shuttleActivity.latitude + "\n" +
                "long: " + shuttleActivity.longitude + "\n" +
                "heading: " + shuttleActivity.heading + "\n"
            );
            console.log(response);
            // TBC : Check assignment for differences and if differences exist, alert driver
            var assignmentData = response.data;
            if (assignmentChanged(assignmentData)) {
              assignment = assignmentData;

              alert('Assignment has been updated by dispatcher.');
              checkPageControlsEnabled();
              bindAssignmentData();
              bindCurrentStatus();
              bindBtnChangeStatusData();
            } else {
              assignment = assignmentData;
            }

          })
          .catch(function(error) {
            console.log(error);
          });
    });
  };

  var assignmentChanged = function(assignmentData) {
    if (assignmentData.shuttleId != assignmentData.shuttleId) return true;
    if (assignmentData.shuttleName != assignmentData.shuttleName) return true;
    if (assignmentData.driverId != assignmentData.driverId) return true;
    if (assignmentData.driverName != assignmentData.driverName) return true;
    if (assignmentData.routeId != assignmentData.routeId) return true;
    if (assignmentData.routeName != assignmentData.routeName) return true;
    if (timeUtils.formatTime(assignmentData.startTime) != timeUtils.formatTime(assignmentData.startTime)) return true;

    if (assignmentData.assignmentReport != null) {
      if (assignment.assignmentReport.assignmentId != assignmentData.assignmentReport.assignmentId) return true;
      if (assignment.assignmentReport.assignmentStops.length != assignmentData.assignmentReport.assignmentStops.length) {
        return true;
      } else {
        for (var i = 0; i < assignmentData.assignmentReport.assignmentStops.length; i++) {
          var oldStop = assignment.assignmentReport.assignmentStops[i];
          var newStop = assignmentData.assignmentReport.assignmentStops[i];

          if(oldStop.assingmentStopId != newStop.assingmentStopId) return true;
          if(oldStop.stopId != newStop.stopId) return true;
          if(oldStop.order != newStop.order) return true;
          if(oldStop.name != newStop.name) return true;
          if(oldStop.address != newStop.address) return true;
          if(timeUtils.formatTime(oldStop.estArriveTime) != timeUtils.formatTime(newStop.estArriveTime)) return true;
          if(timeUtils.formatTime(oldStop.estDepartTime) != timeUtils.formatTime(newStop.estDepartTime)) return true;
        }
      }
    }

    return false;
  };

  var checkPageControlsEnabled = function() {
    var report = assignment.assignmentReport;
    if (report.assignmentStops.length === 0) {
      elements.btnPrev.prop('disabled', true);
      elements.btnNext.prop('disabled', true);
      elements.btnChangeStatus.prop('disabled', true);
      alert('Warning: This assignment has no stops!');
    } else {
      (stopCursor === 0) ? elements.btnPrev.prop('disabled', true) : elements.btnPrev.prop('disabled', false);
      (stopCursor === report.assignmentStops.length - 1) ? elements.btnNext.prop('disabled', true) : elements.btnNext.prop('disabled', false);
    }
  };

  var bindBtnChangeStatusData = function() {
    // TBC : Populate text in change status button to appropriately be for the next logical step
    if (stopCursor != shuttleActivity.currentStopIndex) {
      var nextStop = assignment.assignmentReport.assignmentStops[stopCursor];
      elements.btnChangeStatus.addClass('btn-success');
      elements.btnChangeStatus.removeClass('btn-warning');
      elements.btnChangeStatus.html('Drive to ' + nextStop.name);

      elements.btnChangeStatus.data('nextStopIndex', stopCursor);
      elements.btnChangeStatus.data('nextStatus', DRIVING);
    } else {
      if (shuttleActivity.status == DRIVING) {
        var nextStop = assignment.assignmentReport.assignmentStops[stopCursor];
        elements.btnChangeStatus.addClass('btn-warning');
        elements.btnChangeStatus.removeClass('btn-success');
        elements.btnChangeStatus.html('Stop at ' + nextStop.name);

        elements.btnChangeStatus.data('nextStopIndex', stopCursor);
        elements.btnChangeStatus.data('nextStatus', AT_STOP);
      } else {
        // TBC : At last stop
        if (stopCursor + 1 >= assignment.assignmentReport.assignmentStops.length) {
          var nextStop = assignment.assignmentReport.assignmentStops[stopCursor];
          elements.btnFinishAssignmentContainer.show();
          elements.btnEndAssignmentContainer.hide();

          elements.btnChangeStatus.addClass('btn-success');
          elements.btnChangeStatus.removeClass('btn-warning');
          elements.btnChangeStatus.html('Drive to ' + nextStop.name);

          elements.btnChangeStatus.data('nextStopIndex', stopCursor);
          elements.btnChangeStatus.data('nextStatus', DRIVING);
        } else {
          var nextStop = assignment.assignmentReport.assignmentStops[stopCursor + 1];
          elements.btnEndAssignmentContainer.show();
          elements.btnFinishAssignmentContainer.hide();

          elements.btnChangeStatus.addClass('btn-success');
          elements.btnChangeStatus.removeClass('btn-warning');
          elements.btnChangeStatus.html('Drive to ' + nextStop.name);

          elements.btnChangeStatus.data('nextStopIndex', stopCursor + 1);
          elements.btnChangeStatus.data('nextStatus', DRIVING);
        }
      }
    }
  };

  var bindAssignmentData = function() {
    if (assignment == null) {
      alert('Invalid assignment found!');
    } else {
      elements.routeNameContainer.html(assignment.routeName);

      var report = assignment.assignmentReport;
      if (report != null && report.assignmentStops != null) {
        var stop = report.assignmentStops[stopCursor];

        if (stop == null) {
          alert('Invalid stop');
        } else {
          elements.currentStopContainer.html(stop.name);

          (stop.actualArriveTime != null) ? elements.stopArriveTime.html(timeUtils.formatTime(stop.actualArriveTime) + ' (actual)') : elements.stopArriveTime.html(timeUtils.formatTime(stop.estArriveTime));
          (stop.actualDepartTime != null) ? elements.stopDepartTime.html(timeUtils.formatTime(stop.actualDepartTime) + ' (actual)') : elements.stopDepartTime.html(timeUtils.formatTime(stop.estDepartTime));
        }
      }
    }
  };

  var bindCurrentStatus = function() {
    var stop = assignment.assignmentReport.assignmentStops[shuttleActivity.currentStopIndex];
    elements.labelStatusStop.html(stop.name);

    if (shuttleActivity.status == DRIVING) {
      elements.labelStatus.addClass('btn-success');
      elements.labelStatus.removeClass('btn-warning');

      elements.labelStatus.html('Driving');
      elements.labelStatusPreposition.html('to');
    } else {
      elements.labelStatus.addClass('btn-warning');
      elements.labelStatus.removeClass('btn-success');

      elements.labelStatus.html('At Stop');
      elements.labelStatusPreposition.html('at');
    }
  };

  self.initialize();
  return self;
}
