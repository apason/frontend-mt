 
//This have to be implemented in all activities that does need to show images.
    if(StatusService.StaticStatusService.fh.checkIfImageExists("pictures name")) {
        //Jotain kuvien drawaamista -> jatkoon
    } else { 
        //Jotain
        String... kuvien_urlt_s3ssa_mitä_ei_löydy_kännykästä;
        String... kuvien_tulevat_tiedostonimet_laitteessa;
        //Jotain
        
        new S3Download(listeneri, kuvien_tulevat_tiedostonimet_laitteessa).execute(kuvien_urlt_s3ssa_mitä_ei_löydy_kännykästä) ; 
    }

    
//Joka tapauksessa sitä S3Downloadia voi kustua latailemaan kuvia yksittäin tai sitten monta samalla.