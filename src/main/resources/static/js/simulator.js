function SimulatorApp() {
  const states = {
    idle: 'idle',
    recording: 'recording',
    posting: 'posting'
  };

  var self = this;
  var elements = { };
  var status = states.idle;
  var simLoaded = false;
  var simRecorded = false;
  var shuttleId = 0;
  var shuttleName = null;
  var recordedSim = [];
  var loadedSim = [];
  var intervalId = null;
  var stayAliveIntervalId = null;

  self.initialize = function() {
    elements.simLog = $('#simulation-log');
    log('== SIMULATOR LOG ==');
    log('-------------------');
    log('Simulator Initializing...');

    elements.simStatus = $('#simulation-status');
    elements.shuttleSelect = $('#shuttle-select');
    elements.btnLoadSimulation = $('#btn-load-simulation');
    elements.btnPostLoadedSimulation = $('#btn-post-loaded-simulation');
    elements.statusBtns = $('#status-btns');
    elements.btnActive = $('#btn-active');
    elements.btnDriving = $('#btn-driving');
    elements.btnAtStop = $('#btn-at-stop');
    elements.btnToggleRecording = $('#btn-toggle-recording');
    elements.btnEmptyRecordedSimulation = $('#btn-empty-recorded-simulation');
    elements.btnSaveSimulation = $('#btn-save-simulation');

    elements.btnLoadSimulation.hide();
    elements.btnPostLoadedSimulation.hide();
    elements.statusBtns.hide();
    elements.btnToggleRecording.hide();
    elements.btnEmptyRecordedSimulation.hide();
    elements.btnSaveSimulation.hide();

    updateStatus();
    stayAliveIntervalId = setInterval(stayAlive, 5000);
    bindEventHandlers();
    log('Simulator Initialized');
  };

  var bindEventHandlers = function() {

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

  var updateStatus = function() {
    var msg = 'Status: ' + status + '...';

    if(simLoaded)
      msg += '\n[ simulation loaded : ready for posting ]';
    else
      msg += '\n[ no simulation loaded ]';

    if (shuttleId != 0)
      msg += '\n[ shuttle id=' + shuttleId + ' : ' + shuttleName + ' selected ]';
    else
      msg += '\n[ no shuttle selected ]';

    elements.simStatus.html(msg);
  };

  var log = function(msg) {
    var output = '\n' + new Date().toLocaleTimeString() + ': ' + msg;
    elements.simLog.html(elements.simLog.html() + output);
    console.log(output);
  };

  var disable = function(elem) { elem.prop('disabled', true); };
  var enable = function(elem) { elem.prop('disabled', false); };

  self.initialize();
  return self;
}
