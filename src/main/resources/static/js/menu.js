function MenuApp() {
  var self = this;

  var elements = { };

  self.initialize = function() {
    // TBC : Setup elements
    elements.shuttleSelect = $('#shuttle-select');
    elements.btnBeginService = $('#btn-begin-service');
    elements.activationForm = $('#activation-form');

    elements.btnBeginService.prop('disabled', true);
    bindEventHandlers();
  };

  var bindEventHandlers = function() {
    elements.shuttleSelect.on('change', function() {
      ($(this).val() == 0) ?
          elements.btnBeginService.prop('disabled', true) :
          elements.btnBeginService.prop('disabled', false);
    });

    elements.btnBeginService.on('click', function() {
      if (elements.shuttleSelect.val() != 0) {
        var form = elements.activationForm;
        form.append(getInput('selectedShuttleId', elements.shuttleSelect.val()));
        form.submit();
      } else {
        alert('Please select shuttle.');
      }
    });
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
