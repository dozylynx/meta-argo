# meta-argo : Layer for Argo interdomain communication software

OpenEmbedded layer meta and recipes to enable building Argo software for deployable images, currently with Xen and Linux.

-- Christopher Clark
- updated June 2024

## References

* https://xenbits.xen.org/docs/4.17-testing/designs/argo.html
* https://wiki.xenproject.org/wiki/Argo:_Hypervisor-Mediated_Exchange_(HMX)_for_Xen
* https://openxt.atlassian.net/wiki/spaces/DC/pages/1770422298/HMX+Hypervisor-Mediated+data+eXchange
* https://static.sched.com/hosted_files/xensummit19/92/Argo%20and%20HMX%20-%20OpenXT%20-%20Christopher%20Clark%20-%20Xen%20Summit%202019.pdf
* https://www.youtube.com/watch?v=cnC0Tg3jqJQ

## Build instructions

### Build environment

I recommend using a machine with a linux distribution that OpenEmbedded are happy with. [Devuan](https://devuan.org) is currently close enough and is used in development.

### Obtain the sources

```
export BRANCH="scarthgap"

git clone git://git.yoctoproject.com/poky
cd poky
git checkout "${BRANCH}"

git clone git://git.openembedded.org/meta-openembedded
cd meta-openembedded ; git checkout "${BRANCH}" ; cd -

git clone git://git.yoctoproject.org/meta-virtualization
cd meta-virtualization ; git checkout "${BRANCH}" ; cd -

git clone https://github.com/dozylynx/meta-argo
cd meta-argo ; git checkout "${BRANCH}" ; cd -
```

### Prepare build configuration
```
source ./oe-init-build-env
```

#### Configure bblayers.conf

Locate `conf/bblayers.conf`, and edit the file contents - update `{YOUR FILESYSTEM PATH HERE}` for your development environment:

```
# POKY_BBLAYERS_CONF_VERSION is increased each time build/conf/bblayers.conf
# changes incompatibly
POKY_BBLAYERS_CONF_VERSION = "2"

BBPATH = "${TOPDIR}"
BBFILES ?= ""

BBLAYERS ?= " \
  {YOUR FILESYSTEM PATH HERE}/poky/meta \
  {YOUR FILESYSTEM PATH HERE}/poky/meta-poky \
  {YOUR FILESYSTEM PATH HERE}/poky/meta-yocto-bsp \
  {YOUR FILESYSTEM PATH HERE}/poky/meta-openembedded/meta-oe \
  {YOUR FILESYSTEM PATH HERE}/poky/meta-openembedded/meta-filesystems \
  {YOUR FILESYSTEM PATH HERE}/poky/meta-openembedded/meta-networking \
  {YOUR FILESYSTEM PATH HERE}/poky/meta-openembedded/meta-python \
  {YOUR FILESYSTEM PATH HERE}/poky/meta-virtualization \
  {YOUR FILESYSTEM PATH HERE}/poky/meta-argo \
  "
```

#### Configure local.conf:

Building for x86-64. *ARM can be done, but requires additional configuration outside the scope of this guide*.

Enable Xen inclusion in your local.conf:

```
DISTRO_FEATURES:append = " virtualization xen"
```

To build for deployment onto generic x86-64 hardware, add:

```
MACHINE = "genericx86-64"

```

Note that the `pcengines-apu2` from the [`meta-pcengines`](https://github.com/pcengines/meta-pcengines) layer is also a valid `MACHINE`, if you have updated that layer for current branch compatibility.

Alternatively, to build for testing in qemu, add this:

```
MACHINE = "qemux86-64"
```

These settings can accelerate the build; choose numbers that are appropriate for your hardware.

```
BB_NUMBER_THREADS ?= "4"
PARALLEL_MAKE ?= "-j 2"

```

Docs for these settings:
- [BB_NUMBER_THREADS](https://docs.yoctoproject.org/ref-manual/variables.html#term-BB_NUMBER_THREADS)
- [PARALLEL_MAKE](https://docs.yoctoproject.org/ref-manual/variables.html#term-PARALLEL_MAKE)

*Optional: reminder: set up the download mirror if you have one and know how to do it*


### Build

```
# Building both the host and guest images to deploy to a test machine:
bitbake xen-image-minimal xen-guest-image-minimal

# Just building the components:
bitbake argo-linux-module libargo
```

### Run the Xen host image in Qemu

If you have built an image with `MACHINE` set to `qemux86-64`, then you can
boot it at the command line with:

```
runqemu xen-image-minimal nographic slirp
```

This will launch the image and you can log in and configure guests for testing
as needed.

Note that the plumbing of the hypervisor command line option to enable Argo is
performed by the meta-argo wic configuration file
`qemuboot-xen-argo-x86-64.cfg`.

### Deploy to Hardware Instructions

These steps were written without using the Xen image support for building system
images with wic, so include manual steps for configuring host disk partitions.

#### Install dom0
Steps:

 * Write Xen image minimal onto the disk eg. `/dev/sda`

```
# erase the disk
dd if=/dev/zero of=/dev/sda bs=1M count=10 ; sync

# create a single partition
fdisk /dev/sda

# new partition, start of disk to end of disk,
# t : type 83 (Linux),
# a : set the bootable flag

# populate the partition with the filesystem.
# make sure you use 'xen-image-minimal' and not 'xen-guest-image-minimal'
# as the former is the dom0 image and the latter is a guest VM image.

dd if=xen-image-minimal.ext4 of=/dev/sda1 bs=1M conv=notrunc ; sync

```

Change the Xen command line to add argo -- this is a *pxelinux* config since I used a local pxe server rather than boot directly off the test machine.

```
MENU BEGIN
MENU TITLE argo-tests
LABEL argo-tests
    MENU LABEL argo-tests
    KERNEL mboot.c32
    append argo-tests/xen.gz flask=disabled sync_console argo=1,mac-permissive=1 console=com1,vga dom0_max_vcpus=1 com1=115200,8n1,0x3f8 dom0_mem=max:2G ucode=-1 loglvl=all guest_loglvl=all --- argo-tests/bzImage debug root=/dev/sda1 rw earlyprintk=xen rootwait console=hvc0 --- argo-tests/initrd

MENU END

```

Note that the important entry is **`argo=1,mac-permissive=1`** on the Xen command line.

### Install a PV guest

```
mkdir /home/root/vm
```
Create a new file: `vm1.cfg` with contents:

```
name="vm1"
vcpus=1
memory=800
disk=['file:/home/root/vm/disk1.img,xvda,w']
vif=['bridge=xenbr0','mac=00:16:3E:66:83:76']
on_reboot="destroy"
on_crash="destroy"
kernel="/home/root/vm/vmlinuz"
ramdisk="/home/root/vm/initrd"
extra="root=/dev/xvda debug rw rootfstype=ext4 debugshell"
```

```
cp xen-guest-image-minimal.ext4 /home/root/vm/disk1.img
# get the guest kernel from the xen-guest-image-minimal deploy directory from the build
cp vmlinuz /home/root/vm/vmlinuz
# also get the initrd
cp initrd /home/root/vm/initrd
```

Patch the init script in the initrd so that it can find `/dev/xvda`
in the loop just after `echo "Waiting for removable media..."`:

```
--- init-live.sh    2018-12-03 22:05:47.624593311 -0800
+++ init.xvda   2018-12-03 22:06:11.272593311 -0800
@@ -137,6 +137,10 @@
        break   
       fi
   done
+
+  ROOT_DISK="/dev/xvda"
+  found="yes"
+
   if [ "$found" = "yes" ]; then
       break;
   fi
```

Boot it:
```
xl create -c vm1.cfg
```

#### Testing Argo via the Linux kernel driver and userspace interposer

Within the guest:
```
# load the Argo kernel module:
insmod /lib/modules/*/extra/argo.ko

# use the interposer to run the ssh server using example port 8022
export INET_IS_ARGO=1 ; LD_PRELOAD=/usr/lib/libargo-1.0.so.0.0.0 /usr/sbin/sshd -p 8022

# use the interposer to run the ssh client to connect to the server on the example port 8022
export INET_IS_ARGO=1 ; LD_PRELOAD=/usr/lib/libargo-1.0.so.0.0.0 /usr/bin/ssh localhost -p 8022
```

### Install a HVM guest

Enable HVM guest networking in dom0 by loading the tun/tap kernel module:
```
modprobe tun
```

Create a new file: `vm2.cfg` with contents:
```
name="vm2"
vcpus=1
memory=800
disk=['file:/home/root/vm/disk2.img,xvda,w']
vif=['bridge=xenbr0','mac=00:16:3E:11:22:42']
on_reboot="destroy"
on_crash="destroy"
type="hvm"
builder='hvm'
firmware="/usr/lib/xen/boot/hvmloader"
sdl=0
vnc=1
vnclisten="0.0.0.0"
vncpasswd='password'
device_model_version="qemu-xen"
device_model_override="/usr/bin/qemu-system-x86_64"
serial='pty'
boot='dc'
```

You will need to populate the file `/home/root/vm/disk2.img` as a disk image with a partition table and bootloader and the contents of the `xen-guest-image-minimal` filesystem on a partition.

You can access the test VM by using a VNC client to interact with it.

One way to populate the VM disk is to modify `vm2.cfg` to attach a distro Live CD / installer ISO image, boot and install that and then use that running VM system to populate a second attached disk within the VM with the guest bits that you need. Not much fun and there should be an easier method but as an expedient way to get it done, that works.

Testing argo works the same within the HVM guest as it does within the PV one -- see instructions above.
