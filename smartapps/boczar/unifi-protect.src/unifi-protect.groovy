/**
 *  Unifi Protect
 *
 *  Copyright 2020 Alexander Boczar
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Unifi Protect",
    namespace: "boczar",
    author: "Alexander Boczar",
    description: "Unifi Protect Controller",
    category: "My Apps",
    iconUrl: "https://prd-www-cdn.ubnt.com/media/images/productgroup/unifi-cloud-key-gen2/usg-g2-small.png",
    iconX2Url: "https://prd-www-cdn.ubnt.com/media/images/productgroup/unifi-cloud-key-gen2/usg-g2-small.png",
    iconX3Url: "https://prd-www-cdn.ubnt.com/media/images/productgroup/unifi-cloud-key-gen2/usg-g2-small.png") {
    appSetting "clientId"
}


preferences {
	section("Title") {
		// TODO: put inputs here
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
}

// TODO: implement event handlers