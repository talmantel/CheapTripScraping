class ErrorHandler extends Error {
    constructor(statusCode, message) {
      super();
      this.statusCode = statusCode ? statusCode : 500;
      this.message = message;
    }
  }
  
  const handleError = (err, res) => {
    let { statusCode, message } = err;
    statusCode = statusCode || 500;
    res.status(statusCode).json({
      status: 'error',
      statusCode,
      message,
    });
  };
  
  module.exports = {
    ErrorHandler,
    handleError,
  };