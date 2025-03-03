async function uploadFile() {
    const fileInput = document.getElementById("fileInput");
    const file = fileInput.files[0];

    if (!file) {
        alert("Please select a file!");
        return;
    }

     try {
            // Step 1: Get Pre-Signed URL from Backend
            const response = await fetch("/upload", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                }
            });

            if (!response.ok) {
                throw new Error(`Failed to get presigned URL: ${response.status}`);
            }

            const presignedUrl = await response.text();

            // Step 2: Upload file to S3 using the Pre-Signed URL
            const uploadResponse = await fetch(presignedUrl, {
                method: "PUT",
                body: file,
                headers: {
                    "Content-Type": "application/pdf"
                }
            });

            if (!uploadResponse.ok) {
                throw new Error(`Upload failed: ${uploadResponse.status}`);
            }

            alert("File uploaded successfully!");
        } catch (error) {
            console.error("Error:", error);
            alert("Error: " + error.message);
        }
}

function getPin() {
            const inputs = document.querySelectorAll(".pin-input input");
            let pin = "";
            inputs.forEach(input => pin += input.value);
            return pin;
}

function downloadFile() {
    const key = getPin();

    if (!key || key.length !== 4) {
        alert("Please enter a valid key.");
        return;
    }

    // Make a GET request to the Spring Boot backend
    const url = `/download?key=${key}`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error("Please enter a valid key");
            }
            return response.blob();
        })
        .then(blob => {
            // Create a temporary URL for the blob
            const downloadUrl = window.URL.createObjectURL(blob);

            // Create a temporary link element
            const downloadLink = document.createElement('a');
            downloadLink.href = downloadUrl;
            downloadLink.download = `${key}.pdf`; // Set the download filename

            // Append to document, click, and remove
            document.body.appendChild(downloadLink);
            downloadLink.click();
            document.body.removeChild(downloadLink);

            // Clean up the temporary URL
            window.URL.revokeObjectURL(downloadUrl);
        })
        .catch(error => {
            alert(error.message);
        });
}