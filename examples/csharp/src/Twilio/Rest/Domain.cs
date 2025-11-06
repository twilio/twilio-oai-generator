using Twilio.Types;

namespace Twilio.Rest
{
    public sealed class Domain : StringEnum
    {
        private Domain(string value) : base(value) {}
        public Domain() {}
        public static implicit operator Domain(string value)
        {
            return new Domain(value);
        }

        public static readonly Domain Api = new Domain("api");
        public static readonly Domain FlexApi = new Domain("flex-api");
        public static readonly Domain Versionless = new Domain("versionless");
        public static readonly Domain PreviewIam = new Domain("preview-iam");
        public static readonly Domain Oauth = new Domain("oauth");
    }
}
