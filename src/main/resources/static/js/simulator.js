function SimulatorApp() {
  const states = {
    idle: 'idle',
    recording: 'recording',
    simulating: 'simulating'
  };

  var self = this;
  var elements = { };
  var status = states.idle;
  var recordedSim = [];
  var loadedSim = [];
  var simCursor = 0;
  var intervalId = null;
  var stayAliveIntervalId = null;
  var geoLocator = null;

  var shuttleId = 0;
  var shuttleName = null;
  var shuttleStatus = null;
  var heading = 0;

  self.initialize = function() {
    elements.simLog = $('#simulation-log');
    log('== SIMULATOR LOG ==');
    log('-------------------');
    log('Simulator Initializing...');

    elements.simStatus = $('#simulation-status');
    elements.shuttleSelect = $('#shuttle-select');
    elements.btnLoadSimulation = $('#btn-load-simulation');
    elements.btnToggleSimulating = $('#btn-toggle-simulating');
    elements.statusBtns = $('#status-btns');
    elements.btnActive = $('#btn-active');
    elements.btnDriving = $('#btn-driving');
    elements.btnAtStop = $('#btn-at-stop');
    elements.btnToggleRecording = $('#btn-toggle-recording');
    elements.btnEmptyRecordedSimulation = $('#btn-empty-recorded-simulation');
    elements.btnSaveSimulation = $('#btn-save-simulation');

    geoLocator = new GeoLocator();

    shuttleId = elements.shuttleSelect.val();
    shuttleName = elements.shuttleSelect.find('option:selected').text();
    shuttleStatus = elements.statusBtns.find('.selected').data('status');

    stayAliveIntervalId = setInterval(stayAlive, 3000);
    refreshStatus();
    refreshButtons();

    bindEventHandlers();
    log('Simulator Initialized');
  };

  var bindEventHandlers = function() {
    elements.shuttleSelect.on('change', function(){
      shuttleId = $(this).val();

      if (shuttleId == 0) {
        shuttleName = '';
        log("No shuttle selected");
      } else {
        shuttleName = $(this).find('option:selected').text();
        log("Shuttle id=" + shuttleId + ' - ' + shuttleName + ' selected');
      }

      refreshButtons();
      refreshStatus();
    });

    elements.btnLoadSimulation.on('click', function(){
      loadSimulation();
      refreshButtons();
      refreshStatus();
    });

    elements.btnToggleSimulating.on('click', function(){
      if (status === states.idle) {
        log('Simulation started...');
        simCursor = 0;
        status = states.simulating;
        simulateCycle();
        intervalId = setInterval(simulateCycle, 3000);
      } else if (status === states.simulating) {
        stopSimulation();
      }
      refreshButtons();
      refreshStatus();
    });

    elements.statusBtns.on('click', 'button', function(){
      elements.statusBtns.find('.selected').removeClass('selected');
      $(this).addClass('selected');
      shuttleStatus = $(this).data('status');
      refreshButtons();
      refreshStatus();
    });

    elements.btnToggleRecording.on('click', function(){
      if (status === states.idle) {
        log('Recording started...');
        status = states.recording;
        recordCycle();
        intervalId = setInterval(recordCycle, 3000);
      } else if (status === states.recording) {
        log('Recording stopped');
        status = states.idle;
        clearInterval(intervalId);
      }
      refreshButtons();
      refreshStatus();
    });

    elements.btnEmptyRecordedSimulation.on('click', function(){
      recordedSim = [];
      log('Recorded simulation emptied...');
      refreshButtons();
      refreshStatus();
    });

    elements.btnSaveSimulation.on('click', function(){
      saveSimulation();
      refreshButtons();
      refreshStatus();
    });

    Compass.watch(function (_heading) {
      heading = _heading;
    });
  };

  var recordCycle = function() {
    geoLocator.getLocation().done(function(position) {
      var o = {
        shuttleId: shuttleId,
        latitude: position.coords.latitude,
        longitude: position.coords.longitude,
        heading: heading,
        status: shuttleStatus
      };

      recordedSim.push(o);
      log('cycle added...' +
          '\nshuttle: ' + o.shuttleId +
          '\nlat: ' + o.latitude +
          '\nlong: ' + o.longitude +
          '\nheading: ' + o.heading +
          '\nstatus: ' + shuttleStatus
      );

      refreshButtons();
      refreshStatus();
    });
  };

  var simulateCycle = function() {
    var o = loadedSim[simCursor];

    axios.post('/api/postSimulation', o)
        .then(function(response) {
          log('activity posted...' +
              '\nshuttle: ' + o.shuttleId +
              '\nlat: ' + o.latitude +
              '\nlong: ' + o.longitude +
              '\nheading: ' + o.heading +
              '\nstatus: ' + o.status);

          if (simCursor < loadedSim.length - 1)
            simCursor++;
          else
            simCursor = 0;

          refreshButtons();
          refreshStatus();
        })
        .catch(function(error) {
          log('Simulation post failed: ' + error.message);
        });
  };

  var stopSimulation = function() {
    axios.get('/api/stopSimulation?shuttleId=' + shuttleId)
        .then(function(response) {
          log('Simulation stopped');
          status = states.idle;
          simCursor = 0;
          clearInterval(intervalId);

          refreshButtons();
          refreshStatus();
        })
        .catch(function(error) {
          log('Simulation stop failed: ' + error.message);
        });
  };

  var loadSimulation = function(){
    log('Loading simulation...');
    axios.get('/api/loadSimulation?shuttleId=' + shuttleId)
        .then(function(response) {
          if(response.data.length < 1) {
            if (shuttleId == 0) {
              throw { message: 'No shuttle selected' };
            } else {
              throw { message: 'No Simulation Found' };
            }
          } else {
            loadedSim = response.data;
            log('Simulation loaded successfully');
            refreshButtons();
            refreshStatus();
          }
        })
        .catch(function(error) {
          log('Simulation load failed: ' + error.message);
        });
  };

  var saveSimulation = function() {
    log('Saving simulation...');
    axios.post('/api/saveSimulation', { cycles: recordedSim })
        .then(function(response) {
          log('Save successful');
          refreshButtons();
          refreshStatus();
        })
        .catch(function(error) {
          log('Save failed: ' + error.message);
        });
  };

  var stayAlive = function() {
    axios.post('/api/stayAlive')
        .then(function(response) {
          console.log(response);
        })
        .catch(function(error) {
          console.log(error);
        });
  };

  var refreshButtons = function() {
    elements.statusBtns.hide();
    elements.btnActive.hide();
    elements.btnDriving.hide();
    elements.btnAtStop.hide();
    elements.btnLoadSimulation.hide();
    elements.btnToggleSimulating.hide();
    elements.btnToggleRecording.hide();
    elements.btnEmptyRecordedSimulation.hide();
    elements.btnSaveSimulation.hide();

    if (status === states.recording) {
      disable(elements.shuttleSelect);
      elements.statusBtns.show();
      elements.btnActive.show();
      elements.btnDriving.show();
      elements.btnAtStop.show();
      elements.btnToggleRecording.show();
      elements.btnToggleRecording.html('Stop recording...');
    } else if (status === states.simulating) {
      elements.btnToggleSimulating.show();
      elements.btnToggleSimulating.html('Stop simulating...')

    } else {
      enable(elements.shuttleSelect);

      elements.btnLoadSimulation.show();

      if (loadedSim.length > 0) {
        elements.btnToggleSimulating.show();
        elements.btnToggleSimulating.html('Start simulating')
      }

      if (shuttleId != 0) {
        elements.btnToggleRecording.show();
        elements.btnToggleRecording.html('Start recording');
      }

      if (recordedSim.length > 0) {
        elements.btnEmptyRecordedSimulation.show();
        elements.btnSaveSimulation.show();
      }
    }
  };

  var refreshStatus = function() {
    var msg = 'Status: ' + status + '...';

    msg += '\n[ ' + recordedSim.length + ' cycles recorded ]';

    if (shuttleId != 0)
      msg += '\n[ shuttle id=' + shuttleId + ' : ' + shuttleName + ' selected ]';
    else
      msg += '\n[ no shuttle selected ]';

    if(loadedSim.length > 0)
      msg += '\n[ simulation loaded : ready to simulate ]';
    else
      msg += '\n[ no simulation loaded ]';

    elements.simStatus.html(msg);
  };

  var log = function(msg) {
    var output = '\n' + new Date().toLocaleTimeString() + ': ' + msg;
    elements.simLog.html(elements.simLog.html() + output);
    elements.simLog.scrollTop(elements.simLog.prop('scrollHeight'));
    console.log(output);
  };

  var disable = function(elem) { elem.prop('disabled', true); };
  var enable = function(elem) { elem.prop('disabled', false); };

  self.initialize();
  return self;
}
