package com.gp4.homefinder.data.models

class DistanceResponse (
    var destination_addresses:List<String>,
    var origin_addresses:List<String>,
    var rows: List<DistanceResponseContainer>
        )
{
    class DistanceResponseContainer(
        var elements: List<DistanceResponseContainerBody>
    )

    class DistanceResponseContainerBody(
        var distance: DistanceResponseContainerBodyValue,
        var duration: DistanceResponseContainerBodyValue,
        var status: String
    )

    class DistanceResponseContainerBodyValue(
        var text: String,
        var value: String,
    )
}

