function shouldFire(triggerParameters) {
	if (!triggerParameters.message || !triggerParameters.players) {
		return false;
	}
	
	if (triggerParameters.message() !== 'some message') {
		return false;
	}
	
	if (!triggerParameters.players()[0]) {
		return false;
	}
	
	if (triggerParameters.players()[0].name() !== 'Player1') {
		return false;
	}
	
	return true;
}