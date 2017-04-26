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
  var recordCursor = 0;
  var simCursor = 0;
  var intervalId = null;
  var stayAliveIntervalId = null;

  var shuttleId = 0;
  var shuttleName = null;
  var shuttleStatus = null;

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

    shuttleId = elements.shuttleSelect.val();
    shuttleStatus = elements.statusBtns.find('.selected').data('status');

    stayAliveIntervalId = setInterval(stayAlive, 5000);
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
        intervalId = setInterval(simulateCycle, 5000);
      } else if (status === states.simulating) {
        log('Simulation stopped');
        status = states.idle;
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
        intervalId = setInterval(recordCycle, 5000);
      } else if (status === states.recording) {
        log('Recording stopped');
        status = states.idle;
      }
      refreshButtons();
      refreshStatus();
    });

    elements.btnEmptyRecordedSimulation.on('click', function(){
      recordedSim.empty();
      recordCursor = 0;
      log('Recorded simulation emptied...');
      refreshButtons();
      refreshStatus();
    });

    elements.btnSaveSimulation.on('click', function(){
      saveSimulation();
      refreshButtons();
      refreshStatus();
    });
  };

  var recordCycle = function() {

  };

  var simulateCycle = function() {

  };

  var loadSimulation = function(){
    log('Loading simulation...');
  };

  var saveSimulation = function() {
    log('Saving simulation...');
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

      if (shuttleId != 0 && loadedSim.length <= 0)
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

    if (status === states.recording)
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
