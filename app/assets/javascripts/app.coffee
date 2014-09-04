
dependencies = [
    'ngRoute',
    'ui.bootstrap',
    'packageCenter.filters',
    'packageCenter.services',
    'packageCenter.controllers',
    'packageCenter.directives',
    'packageCenter.common',
    'packageCenter.routeConfig'
]

app = angular.module('packageCenter', dependencies)

angular.module('packageCenter.routeConfig', ['ngRoute'])
    .config ($routeProvider) ->
        $routeProvider
            .when('/', {
                templateUrl: '/assets/partials/packages/summary.html'
            })
            .otherwise({redirectTo: '/'})

@commonModule = angular.module('packageCenter.common', [])
@controllersModule = angular.module('packageCenter.controllers', [])
@servicesModule = angular.module('packageCenter.services', [])
@modelsModule = angular.module('packageCenter.models', [])
@directivesModule = angular.module('packageCenter.directives', [])
@filtersModule = angular.module('packageCenter.filters', [])