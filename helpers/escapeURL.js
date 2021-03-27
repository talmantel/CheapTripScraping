const escapeURL = (url) => {
    // replaceAll doesn't work
   return url.replace(/\s/g, '%20');
}

module.exports = escapeURL;  