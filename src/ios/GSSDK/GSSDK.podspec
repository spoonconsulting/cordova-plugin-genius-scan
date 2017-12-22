#
# Be sure to run `pod lib lint GSSDK.podspec' to ensure this is a
# valid spec before submitting.
#
# Any lines starting with a # are optional, but their use is encouraged
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#

Pod::Spec.new do |s|
  s.name             = 'GSSDK'
  s.version          = '2.0'
  s.summary          = 'Genius Scan SDK'

# This description is used to generate tags and improve search results.
#   * Think: What does it do? Why did you write it? What is the focus?
#   * Try to keep it short, snappy and to the point.
#   * Write the description between the DESC delimiters below.
#   * Finally, don't worry about the indent, CocoaPods strips it!

  s.description      = <<-DESC
                       The Genius Scan SDK
                       * Image Processing routines
                       * UI elements

                       DESC

  s.author           = { 'The Grizzly Labs' => 'contact@thegrizzlylabs.com' }
  s.social_media_url = 'https://twitter.com/thegrizzlylabs'
  s.source           = { :git => "https://github.com/<GITHUB_USERNAME>/GSCamera.git", :tag => s.version.to_s }
  s.homepage         = "http://thegrizzlylabs.com"

  s.ios.deployment_target = '8.0'

  s.vendored_frameworks = 'GSSDK.framework'
  s.dependency 'OpenCV', '~> 3.1'

end
