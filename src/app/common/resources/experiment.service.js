var moment = require('moment');

/* @ngInject */
function experimentService($resource, permissionService, apiUrl) {
    return $resource(apiUrl + 'projects/:projectId/notebooks/:notebookId/experiments/:experimentId',
        {
            projectId: '@projectId',
            notebookId: '@notebookId',
            experimentId: '@experimentId'
        }, {
            query: {
                method: 'GET',
                isArray: true,
                transformResponse: function(data) {
                    var experiment = angular.fromJson(data);
                    _.forEach(experiment, function(item, key) {
                        experiment[key] = toModel(item);
                    });

                    return experiment;
                }
            },
            get: {
                method: 'GET',
                transformResponse: transformResponse
            },
            save: {
                method: 'POST',
                transformRequest: transformRequest,
                transformResponse: transformResponse
            },
            version: {
                method: 'POST',
                url: apiUrl + 'projects/:projectId/notebooks/:notebookId/experiments/:experimentId/version'
            },
            update: {
                method: 'PUT',
                url: apiUrl + 'projects/:projectId/notebooks/:notebookId/experiments',
                transformRequest: transformRequest,
                transformResponse: transformResponse
            },
            delete: {method: 'DELETE'},
            print: {
                method: 'GET',
                url: apiUrl + 'print/project/:projectId/notebook/:notebookId/experiment/:experimentId'
            },
            reopen: {
                method: 'PUT',
                url: apiUrl + 'projects/:projectId/notebooks/:notebookId/experiments/:experimentId/reopen'
            }
        });

    function toModel(experiment) {
        var components = experiment.components;
        if (_.isArray(components)) {
            experiment.components = {};

            _.forEach(components, function(component) {
                experiment.components[component.name] = component.content;
            });

            return experiment;
        }

        return experiment;
    }

    function toComponents(model) {
        return _.map(model, function(val, key) {
            return {
                name: key, content: val
            };
        });
    }

    function transformRequest(data) {
        var copiedRequest = angular.copy(data);
        copiedRequest.components = toComponents(copiedRequest.components);
        copiedRequest.accessList = permissionService.expandPermission(copiedRequest.accessList);

        return angular.toJson(copiedRequest);
    }

    function transformResponse(data) {
        var experiment = toModel(angular.fromJson(data));

        experiment.creationDate = moment(experiment.creationDate).toISOString();

        if (!_.get(experiment, 'components.stoichTable.products')) {
            return experiment;
        }
        _.forEach(experiment.components.stoichTable.products, function(batch) {
            if (!batch.$$batchHash) {
                batch.$$batchHash = batch.formula.value + batch.exactMass;
            }
        });

        return experiment;
    }
}

module.exports = experimentService;
