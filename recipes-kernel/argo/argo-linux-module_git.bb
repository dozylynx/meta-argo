SUMMARY = "Argo Linux module."
DESCRIPTION = "Argo implements hypervisor-mediated data exchange inter-domain \
communication on the Xen hypervisor. \
This Argo Linux kernel module defines a stream and a datagram protocol."
HOMEPAGE = "https://wiki.xenproject.org/wiki/Argo:_Hypervisor-Mediated_Exchange_(HMX)_for_Xen"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

require argo-version.inc

S = "${WORKDIR}/git/argo-linux"

inherit module

EXTRA_OEMAKE += "INSTALL_HDR_PATH=${D}${prefix}"
MODULES_INSTALL_TARGET += "headers_install"

KERNEL_MODULE_AUTOLOAD += "xen-argo"
