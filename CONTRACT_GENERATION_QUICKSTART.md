# Job Contract Image Generation - Quick Start Guide

## Feature Overview
The job contract form now includes the ability to generate, download, and share contract images.

## How to Use

### Step 1: Create or Edit a Contract
1. Navigate to **Job Contract** page (`/gen-bill`)
2. Fill in all the contract details:
   - Weaver Name
   - Trader Name
   - Quality
   - Quantity (Meters)
   - Beams
   - Job Rate
   - Payment Days
   - Production Schedule
   - No. of Machines
   - Remarks
   - Cut Length
   - Minimum Delivery
   - Rolling/Folding preference
   - Sizing/Fabric

3. Click **SUBMIT** button to save the contract

### Step 2: Generate Contract Image
1. After saving, you'll be redirected back to the contract edit page
2. You'll see a new green button labeled **"GENERATE CONTRACT"** 
   - (This button only appears after the contract is saved)
3. Click **GENERATE CONTRACT** button
4. Wait for the image to be generated (you'll see a loading spinner)

### Step 3: Download or Share the Contract

#### Download the Contract:
1. In the modal that appears showing your contract image
2. Click the **DOWNLOAD** button (blue icon)
3. The image will be downloaded as `Contract_{contractNo}_{weaverName}.png`

#### Share the Contract:
1. In the same modal, click the **SHARE** button (green icon)
2. Choose your sharing method:
   - **Native Share** (if your device supports it) - Opens system share options
   - **WhatsApp** - Opens WhatsApp with contract reference
   - **Copy Image** - Copies the image to your clipboard for pasting elsewhere

## What's Included in the Generated Image

The generated contract image includes:

✓ Company header (|| SHREE GANESH ||, Airjet Loom Owner's Association)  
✓ Contract information (Contract No, Contract Date)  
✓ Party details (Weaver, Trader, Broker)  
✓ Job details (Quality, Quantity, Rate, Terms, Machines, etc.)  
✓ Generated timestamp  
✓ Company copyright info  

## Features

| Feature | Details |
|---------|---------|
| **Format** | PNG image (easy to view, share, and print) |
| **Resolution** | High quality (1000x1400 pixels) |
| **Security** | User-specific - can only access own contracts |
| **Sharing** | WhatsApp, clipboard, native share methods |
| **No Dependencies** | Works with all modern browsers |

## Troubleshooting

### "Contract must be saved first before generating image"
- **Solution:** Click SUBMIT to save the contract first, then the GENERATE CONTRACT button will appear

### Image not appearing
- **Solution:** Check your browser console (F12) for errors, ensure JavaScript is enabled

### Download not working
- **Solution:** Check browser download settings, allow popups/downloads from this site

### Share button not working
- **Solution:** Not all browsers support native sharing. Use Copy Image or WhatsApp option instead

### Image quality is poor
- **Solution:** The image is optimized for screen viewing. For printing, use the DOWNLOAD option and then print the PNG file

## Technical Requirements

- Modern web browser (Chrome, Firefox, Edge, Safari)
- JavaScript enabled
- Download permissions for file saving
- For WhatsApp: WhatsApp Web or WhatsApp installed

## Keyboard Shortcuts

| Action | Shortcut |
|--------|----------|
| Submit Form | Click button (no keyboard shortcut) |
| Generate Image | Click button |
| Download | Click button or right-click → Save As |
| Share | Click button |

## Tips & Tricks

1. **Batch Downloads:** You can have multiple contract images open in different browser tabs and download them all
2. **Email Sharing:** Use "Copy Image" then paste into email
3. **Print Contracts:** Download the image and print it from your image viewer
4. **Archive:** Save downloaded contract images in a folder for records
5. **Mobile Friendly:** Share via WhatsApp directly to clients or partners

## Support

For technical issues or feature requests, contact your system administrator.

---

**Last Updated:** March 2026  
**Version:** 1.0
