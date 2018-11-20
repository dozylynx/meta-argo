IMAGE_INSTALL += " \
    libargo \
    argo-linux-module \
    seabios \
    kernel-module-tun \
    e2fsprogs \
"

# libargo : userspace argo interposer
# argo-linux-module : kernel space argo device driver
# kernel-module-tun : used to support networking for HVM VMs
# e2fsprogs : provides resize2fs, handy for expanding dom0 filesystem
# seabios : TODO: needed? useful?
