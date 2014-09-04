
class PackageController

    constructor: (@$log, @PackageService) ->
        @summaries = []
        @getPackageSummaries()

    getPackageSummaries: () ->
        @PackageService.listPackageSummaries()
        .then(
            (data) =>
                @summaries = data
            ,
            (error) =>
                @$log.error "Unable to get packages: #{error}"
            )

controllersModule.controller('PackageController', PackageController)