//These are things to be added to EACH activity that communicates with a service.
// We do not have any services at the moment.

 
//All activities must have these variables
LocalService commService;
boolean CommunicationBound = false; //false at the beggining

//On onStart() there must be added (after super calling):
Intent intent = new Intent(this, ServiceClass.class); //ServiceClass is the service class.
bindService(intent, CommunicationConnection, Context.BIND_AUTO_CREATE); //CommunicationConnection told in this file

//ON onStop()
if (CommunicationBound) {
    unbindService(CommunicationConnection);
    CommunicationBound = false;
}

//And here comes the big one, must be added somewhere in all activities:
/** Defines callbacks for service binding, passed to bindService() */
private ServiceConnection CommunicationConnection = new ServiceConnection() {

    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
        // We've bound to ServerCommunication, cast the IBinder and get ServerCommunication instance
        LocalBinder binder = (LocalBinder) service;
        commService = binder.getService();
        CommunicationBound  = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            CommunicationBound  = false;
        }
    };
}


/**For calling then methods in the service use:*/
//E.G. commService.something("YOLO");
