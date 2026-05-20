

const steps = document.querySelectorAll(".form-step");

function nextStep(stepNumber){

    steps.forEach(step => {
        step.classList.remove("active");
    });

    document
        .getElementById(`step${stepNumber}`)
        .classList.add("active");

}

function prevStep(stepNumber){

    steps.forEach(step => {
        step.classList.remove("active");
    });

    document
        .getElementById(`step${stepNumber}`)
        .classList.add("active");

}



document
    .getElementById("step1")
    .classList.add("active");