const viewContactModal = document.getElementById("view_contact_modal");
const baseURL="http://localhost:8081";

// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};

/*
 * $targetEl: required
 * options: optional
 */
const contactModal = new Modal(viewContactModal, options, instanceOptions);

function openContactModal() {
    contactModal.show();
}

function closeContactModal() {
    contactModal.hide();
}

async function loadContactdata(contactId){
    fetch(`${baseURL}/api/contact/${contactId}`).then(
        async (response) => {
            let data = await response.json();
            console.log(data);
            openContactModal();
            document.getElementById("contactPicture").src=data.picture;
            document.getElementById("contactName").innerHTML=data.name;
            document.getElementById("contactEmail").innerHTML=data.email;
            document.getElementById("contactPhoneNumber").innerHTML=data.phoneNumber;
            document.getElementById("contactAddress").innerHTML=data.address;
            document.getElementById("contactDescription").innerHTML=data.description;
            if(data.favorite){
                document.getElementById("favorite").innerHTML="<i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i>";
            } else{
                document.getElementById("favorite").innerHTML="Not favorite Contact";
            }

            document.getElementById("contactWebsite").innerHTML=data.websiteLink;
            document.getElementById("contactLinkedin").innerHTML=data.linkedinLink;
        }
    ).catch((error) => console.log(error));
}

async function deleteContact(contactId) {
    const swalWithBootstrapButtons = Swal.mixin({
        customClass: {
          confirmButton: "btn btn-success",
          cancelButton: "btn btn-danger"
        },
        buttonsStyling: false
      });
      swalWithBootstrapButtons.fire({
        title: "Are you sure?",
        text: "Do you want to delete this contact? "+contactId,
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Yes, delete it!",
        cancelButtonText: "No, cancel!",
        reverseButtons: true
      }).then((result) => {
        if (result.isConfirmed) {
            const url = `${baseURL}/user/contacts/delete/`+contactId;
            window.location.replace(url);
          swalWithBootstrapButtons.fire({
            title: "Deleted!",
            text: "Your contact has been deleted.",
            icon: "success"
          });
        } else if (
          /* Read more about handling dismissals below */
          result.dismiss === Swal.DismissReason.cancel
        ) {
          swalWithBootstrapButtons.fire({
            title: "Cancelled",
            text: "Your contact is safe :)",
            icon: "error"
          });
        }
      });
}