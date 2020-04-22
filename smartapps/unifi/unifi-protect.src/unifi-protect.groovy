definition(
    name: "UniFi Protect",
    namespace: "unfi",
    author: "Alexander Boczar",
    description: "UniFi Protect",
    iconUrl: "https://prd-www-cdn.ubnt.com/media/images/productgroup/unifi-cloud-key-gen2/usg-g2-small.png",
    oauth: true,
    usesThirdPartyAuthentication: true)
    {
        appSettings "clientId"
    }


preferences {
    page(name: "authPage")
}


def authPage()
{
    def redirectUrl = "https://account.ui.com/login"
    return dynamicPage(name: "Credentials", title: "Connect to Ubiquiti", nextPage: null, uninstall: true, install: true)
    {
        section { href(url:redirectUrl, required: true: title:"Title", description: "Connect")}
    }
}

def oauthInit()
{
    log.info "oauthInit()"
}

def oauthCallback()
{
    log.info "oauthCallback()"
}

def oathReceiveToken(redirectUrl = null)
{
    log.info "oathReceiveToken()"
}

def oauthSuccess()
{
    log.info "oauthSuccess()"
}

def oauthFailure()
{
    log.info "oauthFailure()"
}

def oauthConnectionStatus(message, redirectUrl = null) 
{
    log.info "oauthConnectionStatus(message: ${message}, redirectUrl: ${redirectUrl})"
}

mappings {
    path("/oauth/initialize") { action: [GET: "oauthInitUrl"]}
    path("/oauth/callback") {action: [GET: "callback"]}
}

/**
 * installed() - Called by ST platform
 */
def installed() {
    log.info "installed()"
}

/**
 * updated() - Called by ST platform
 */
def updated() {
    log.info "updated()"
}
