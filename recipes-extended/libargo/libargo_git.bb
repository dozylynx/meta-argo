DESCRIPTION = "Xen Argo communication library and interposer"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen argo-module-headers"

PV = "git${SRCPV}"

#SRCREV = "${AUTOREV}"
SRCREV = "f58254231ce618ed77c21676178dd71ddb11dd1c"
SRC_URI = "git://github.com/dozylynx/meta-argo-linux;branch=rocko"
SRC_URI[md5sum] = "2c2a94168ceca463f6c60326d881f9be"
SRC_URI[sha256sum] = "a09fe06ba79eb52a2a11c05d71afdf23a335d6a4c0265c96814669ffc827dcda"

S = "${WORKDIR}/git/src/libargo"

inherit autotools-brokensep pkgconfig lib_package

EXTRA_OECONF += "--with-pic"

do_install_append() {
    install -d ${D}/etc
    install -d ${D}/etc/udev
    install -d ${D}/etc/udev/rules.d
    install ${S}/13-argo.rules ${D}/etc/udev/rules.d
}
