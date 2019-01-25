SUMMARY = "Argo Linux module."
DESCRIPTION = "Argo implements hypervisor-mediated data exchange inter-domain \
communication on the Xen hypervisor. \
This Argo Linux kernel module defines a stream and a datagram protocol."
HOMEPAGE = "https://wiki.xenproject.org/wiki/Argo:_Hypervisor-Mediated_Exchange_(HMX)_for_Xen"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

PV = "git${SRCPV}"

SRC_URI = "git://github.com/dozylynx/meta-argo-linux;branch=rocko"
#SRCREV = "${AUTOREV}"
SRCREV = "30a2fbe7f5f0fe93a1efd9f7d54617025676eaaf"
SRC_URI[md5sum] = "2c2a94168ceca463f6c60326d881f9be"
SRC_URI[sha256sum] = "a09fe06ba79eb52a2a11c05d71afdf23a335d6a4c0265c96814669ffc827dcda"


S = "${WORKDIR}/git/src/argo-linux"

inherit module
#inherit module-signing

# FIXME: tune this:
DEPENDS += " elfutils-native elfutils openssl-native util-linux-native"
