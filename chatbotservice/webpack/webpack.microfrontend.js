const ModuleFederationPlugin = require('webpack/lib/container/ModuleFederationPlugin');
const packageJson = require('../package.json');
const appVersion = packageJson.version;

module.exports = (config, options, targetOptions) => {
  return {
    plugins: [
      new ModuleFederationPlugin({
        name: 'chatbotservice',
        filename: 'remoteEntry.js',
        exposes: {
          './translation-module': 'app/shared/language/translation.module.ts',
          './entity-navbar-items': 'app/entities/entity-navbar-items.ts',
          './entity-routing': 'app/entities/entity-routing.module.ts',
        },
        shared: {
          // { eager?, import?, packageName?, requiredVersion?, shareKey?, shareScope?, singleton?, strictVersion?, version? }
          '@angular/core': { singleton: true, strictVersion: true, requiredVersion: '14.2.0' },
          '@angular/common': { singleton: true, strictVersion: true, requiredVersion: '14.2.0' },
          '@angular/common/http': { singleton: true, strictVersion: true, requiredVersion: '14.2.0' },
          '@angular/router': { singleton: true, strictVersion: true, requiredVersion: '14.2.0' },
          '@angular/forms': { singleton: true, strictVersion: true, requiredVersion: '14.2.0' },
          '@angular/platform-browser': { singleton: true, strictVersion: true, requiredVersion: '14.2.0' },
          '@fortawesome/angular-fontawesome': { singleton: true, strictVersion: true, requiredVersion: '0.11.1' },
          '@fortawesome/fontawesome-common-types': { singleton: true, strictVersion: true },
          '@fortawesome/fontawesome-svg-core': { singleton: true, strictVersion: true, requiredVersion: '6.2.0' },
          '@fortawesome/free-solid-svg-icons': { singleton: true },
          '@ng-bootstrap/ng-bootstrap': { singleton: true, strictVersion: true, requiredVersion: '13.0.0' },
          '@ngx-translate/core': { singleton: true, strictVersion: true, requiredVersion: '14.0.0' },
          '@ngx-translate/http-loader': { singleton: true, strictVersion: true, requiredVersion: '7.0.0' },
          dayjs: { singleton: true, strictVersion: true, requiredVersion: '1.11.5' },
          'ngx-infinite-scroll': { singleton: true, strictVersion: true, requiredVersion: '14.0.0' },
          'ngx-webstorage': { singleton: true, strictVersion: true, requiredVersion: '10.0.1' },
          rxjs: { singleton: true, strictVersion: true, requiredVersion: '7.5.6' },
          'rxjs/operators': { singleton: true, strictVersion: true, requiredVersion: '7.5.6' },
          'zone.js': { singleton: true, strictVersion: true, requiredVersion: '0.11.6' },
          'app/config/input.constants': {
            singleton: true,
            import: 'app/config/input.constants',
            requiredVersion: appVersion,
          },
          'app/config/pagination.constants': {
            singleton: true,
            import: 'app/config/pagination.constants',
            requiredVersion: appVersion,
          },
          'app/config/translation.config': {
            singleton: true,
            import: 'app/config/translation.config',
            requiredVersion: appVersion,
          },
          'app/core/auth': {
            singleton: true,
            import: 'app/core/auth',
            requiredVersion: appVersion,
          },
          'app/core/config': {
            singleton: true,
            import: 'app/core/config',
            requiredVersion: appVersion,
          },
          'app/core/interceptor': {
            singleton: true,
            import: 'app/core/interceptor',
            requiredVersion: appVersion,
          },
          'app/core/request': {
            singleton: true,
            import: 'app/core/request',
            requiredVersion: appVersion,
          },
          'app/core/util': {
            singleton: true,
            import: 'app/core/util',
            requiredVersion: appVersion,
          },
          'app/shared': {
            singleton: true,
            import: 'app/shared',
            requiredVersion: appVersion,
          },
          'app/shared/alert': {
            singleton: true,
            import: 'app/shared/alert',
            requiredVersion: appVersion,
          },
          'app/shared/auth': {
            singleton: true,
            import: 'app/shared/auth',
            requiredVersion: appVersion,
          },
          'app/shared/date': {
            singleton: true,
            import: 'app/shared/date',
            requiredVersion: appVersion,
          },
          'app/shared/language': {
            singleton: true,
            import: 'app/shared/language',
            requiredVersion: appVersion,
          },
          'app/shared/pagination': {
            singleton: true,
            import: 'app/shared/pagination',
            requiredVersion: appVersion,
          },
          'app/shared/sort': {
            singleton: true,
            import: 'app/shared/sort',
            requiredVersion: appVersion,
          },
        },
      }),
    ],
    output: {
      publicPath: 'auto',
      uniqueName: 'chatbotservice',
      scriptType: 'text/javascript',
    },
    optimization: {
      runtimeChunk: false,
    },
  };
};
