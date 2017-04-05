function ShuttleActivity() {
  var self = this;

  self.shuttleId = 0;
  self.driverId = 0;
  self.assignmentId = 0;
  self.latitude = 0.0;
  self.longitude = 0.0;
  self.currentStopIndex = 0;
  self.heading = 0.0;
  self.status = 'NONE';
  self.latitude = null;
  self.longitude = null;

  return self;
}
