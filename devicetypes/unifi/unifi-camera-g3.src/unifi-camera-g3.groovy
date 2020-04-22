metadata {
    definition (name: "UniFi Camera G3", namespace: "unifi", author: "Alexander Boczar") {
        capability "Motion Sensor"
        capability "Sensor"
        capability "Refresh"
        capability "Image Capture"
    }
    
    simulator {
        
    }
    
    tiles( scale: 2 ) {
        carouselTile("cameraSnapshot", "device.image", width: 6, height: 4) { }
        
        standardTile("take", "device.image", width: 2, height: 2, canChangeIcon: false, inactiveLabel: true, canChangeBackground: false) {
            state "take", label: "Take", action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF", nextState:"taking"
            state "taking", label:'Taking', action: "", icon: "st.camera.take-photo", backgroundColor: "#53a7c0"
            state "image", label: "Take", action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF", nextState:"taking"
        }
        
        standardTile("motion", "device.motion", width: 2, height: 2) {
            state("active", label:'motion', icon:"st.motion.motion.active", backgroundColor:"#53a7c0")
            state("inactive", label:'no motion', icon:"st.motion.motion.inactive", backgroundColor:"#ffffff")
        }
        
        standardTile( "connectionStatus", "device.connectionStatus", width: 2, height: 2 ) {
            state( "CONNECTED", label: "Connected", icon: "st.samsung.da.RC_ic_power", backgroundColor: "#79b821" )
            state( "DISCONNECTED", label: "Disconnected", icon: "st.samsung.da.RC_ic_power", backgroundColor: "#ffffff" )
        }
        
        main( "motion" )
        details( "cameraSnapshot", "take", "motion", "connectionStatus" )
    }
    
    preferences {
        input "pollInterval", "number", title: "Poll Interval", description: "Polling interval in seconds for motion detection", defaultValue: 5
        input "snapOnMotion", "bool", title: "Snapshot on motion", description: "If enabled, take a snapshot when the camera detects motion", defaultValue: false
    }
}

/**
 * installed()
 *
 * Called by ST platform.
 */
def installed()
{
    log.info "installed()"

    updated()
}

/**
 * updated()
 *
 * Called by ST platform.
 */
def updated()
{
    log.info "updated()"

    // Unschedule here to remove any zombie runIn calls that the platform
    // seems to keep around even if the code changes during dev
    unschedule()
    
    state.name                   = getDataValue( "name" )
    state.id                     = getDataValue( "id" )
    state.lastMotion             = null
    state.motion                 = "uninitialized"
    state.connectionStatus       = "uninitialized"
    state.pollInterval           = settings.pollInterval ? settings.pollInterval : 5
    state.pollIsActive           = false
    state.successiveApiFails     = 0
    state.lastPoll               = new Date().time
    
    log.info "${device.displayName} updated with state: ${state}"
    
    runEvery1Minute( nvr_cameraPollWatchdog )
}

/**
 * refresh()
 * 
 * Called by ST platform, part of "Refresh" capability.  Usually only called when the user explicitly
 * refreshes the device details pane.
 */
def refresh()
{
    log.info "refresh()"

    _sendMotion( state.motion )
    _sendConnectionStatus( state.connectionStatus )
}

/**
 * take()
 *
 * Called by ST platform, part of "Image Capture" capability.
 */
def take()
{
    log.info "take()"

    def key = parent._getApiKey()
    def target = parent._getNvrTarget()
    
    if( state.connectionStatus == "CONNECTED" )
    {
        hubAction = new physicalgraph.device.HubAction(
            [
                path: "/api/cameras/${state.id}/snapshot/?w=640&h=360",
                method: "GET",
                HOST: target,
                headers: [
                    "Host":"${target}",
                    "Accept":"*/*",
                    "Authorization":"Bearer ${key}"
                ]        
            ],
            [
                outputMsgToS3: true,
            ]
        );
    
        hubAction
    }
}

