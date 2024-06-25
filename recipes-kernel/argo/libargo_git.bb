DESCRIPTION = "Xen Argo communication library and interposer"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen argo-module-headers"

require argo-version.inc

S = "${WORKDIR}/git/libargo"

inherit autotools-brokensep pkgconfig lib_package

EXTRA_OECONF += "--with-pic"

do_install:append() {
    install -d ${D}/etc
    install -d ${D}/etc/udev
    install -d ${D}/etc/udev/rules.d
    install ${S}/13-argo.rules ${D}/etc/udev/rules.d
}
