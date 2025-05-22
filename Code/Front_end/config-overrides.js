const webpack = require('webpack');

module.exports = function override(config) {
  config.resolve.fallback = {
    ...config.resolve.fallback,
    "http": false,
    "https": false,
    "stream": false,
    "crypto": false,
    "zlib": false,
    "util": false,
    "url": false,
    "assert": false,
    "fs": false,
    "path": false,
    "os": false
  };

  config.plugins = [
    ...config.plugins,
    new webpack.ProvidePlugin({
      process: 'process/browser',
      Buffer: ['buffer', 'Buffer']
    })
  ];

  return config;
}; 