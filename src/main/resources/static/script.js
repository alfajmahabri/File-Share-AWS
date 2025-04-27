async function uploadFile() {
    const fileInput = document.getElementById("fileInput");
    const file = fileInput.files[0];

    if (!file) {
        alert("Please select a file!");
        return;
    }

    try {
        const formData = new FormData();
        formData.append("file", file);

        const response = await fetch("/upload", {
            method: "POST",
            body: formData
        });

        if (!response.ok) {
            throw new Error(`Failed to get presigned URL: ${response.status}`);
        }

        const data = await response.json();
        const presignedUrl = data.url;
        const pin = data.pin;

        const uploadResponse = await fetch(presignedUrl, {
            method: "PUT",
            body: file,
            headers: {
                "Content-Type": file.type
            }
        });

        if (!uploadResponse.ok) {
            throw new Error(`Upload failed: ${uploadResponse.status}`);
        }

        document.getElementById("generatedPin").innerText = `Your PIN: ${pin}`;
        alert("File uploaded successfully!");
    } catch (error) {
        alert("Error: " + error.message);
    }
}

function getPin() {
    const inputs = document.querySelectorAll(".pin-input input");
    let pin = "";
    inputs.forEach(input => pin += input.value);
    return pin;
}

async function downloadFile(event) {
    if (event) {
        event.preventDefault();
    }

    try {
        const pin = getPin();

        if (!pin || pin.length !== 4) {
            alert("Please enter a valid 4-digit pin.");
            return;
        }

        const response = await fetch(`/download?key=${pin}`);
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const contentType = response.headers.get('content-type');
        if (!contentType || !contentType.includes('application/json')) {
            throw new Error("Response is not JSON");
        }

        const downloadResponse = await response.json();
        const { filename, extension, url: presignedUrl } = downloadResponse;
        if (!filename || !extension || !presignedUrl) {
            throw new Error("Invalid response data: missing filename, extension, or url.");
        }

        const fileResponse = await fetch(presignedUrl);
        if (!fileResponse.ok) {
            throw new Error(`File fetch failed: ${fileResponse.status}`);
        }

        const blob = await fileResponse.blob();
        const downloadUrl = window.URL.createObjectURL(blob);
        const downloadLink = document.createElement('a');
        downloadLink.href = downloadUrl;
        downloadLink.download = `${filename}.${extension}`;
        document.body.appendChild(downloadLink);
        downloadLink.click();
        document.body.removeChild(downloadLink);
        window.URL.revokeObjectURL(downloadUrl);
    } catch (error) {
        alert(`Error: ${error.message}`);
    }
}

document.getElementById('downloadButton').addEventListener('click', downloadFile);