/* @ngInject */
function autorecoveryCache(CacheFactory, principalService) {
    var cache = CacheFactory('recoveryCache', {
        storageMode: 'localStorage',
        // 24 hours
        maxAge: 86400000
    });

    var visbilityAutorecovery = {};

    var tempRecoveryCache = CacheFactory('tempRecoveryCache');

    return {
        put: put,
        putTempRecoveryData: putTempRecoveryData,
        getTempRecoveryData: getTempRecoveryData,
        removeTempRecoveryData: removeTempRecoveryData,
        isResolved: isResolved,
        isVisible: isVisible,
        tryToVisible: tryToVisible,
        hide: hide,
        get: get,
        remove: removeByParams,
        clearAll: clearAll
    };

    function put(stateParams, data) {
        cache.put(paramsConverter(stateParams), data);
    }

    function get(stateParams) {
        return cache.get(paramsConverter(stateParams));
    }

    function putTempRecoveryData(stateParams, data) {
        tempRecoveryCache.put(paramsConverter(stateParams), data);
    }

    function getTempRecoveryData(stateParams) {
        return tempRecoveryCache.get(paramsConverter(stateParams));
    }

    function removeTempRecoveryData(stateParams) {
        tempRecoveryCache.remove(paramsConverter(stateParams));
    }

    function removeByParams(stateParams) {
        cache.remove(paramsConverter(stateParams));
    }

    function clearAll() {
        cache.clearAll();
    }

    function isResolved(stateParams) {
        return !_.isUndefined(visbilityAutorecovery[paramsConverter(stateParams)]);
    }

    function isVisible(stateParams) {
        return visbilityAutorecovery[paramsConverter(stateParams)];
    }

    function tryToVisible(stateParams) {
        if (_.isUndefined(visbilityAutorecovery[paramsConverter(stateParams)])) {
            visbilityAutorecovery[paramsConverter(stateParams)] = true;
        }
    }

    function hide(stateParams) {
        visbilityAutorecovery[paramsConverter(stateParams)] = false;
    }

    function paramsConverter(stateParams) {
        return principalService.getUserId() + angular.toJson(stateParams);
    }
}

module.exports = autorecoveryCache;
