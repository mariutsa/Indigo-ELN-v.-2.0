var i18n = require('./i18en.constant');
var translateService = require('./translate.service');

module.exports = angular
    .module('indigoeln.common.i18n', [])

    .constant('i18en', i18n)
    .factory('translateService', translateService)

    .name;
