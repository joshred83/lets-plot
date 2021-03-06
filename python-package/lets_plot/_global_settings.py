#
# Copyright (c) 2019. JetBrains s.r.o.
# Use of this source code is governed by the MIT license that can be found in the LICENSE file.
#
import os
from typing import Any

from ._version import __version__

# Public environment variables
ENV_HTML_ISOLATED_FRAME = 'LETS_PLOT_HTML_ISOLATED_FRAME'  # bool
ENV_DEV_HTML_ISOLATED_FRAME = 'LETS_PLOT_DEV_HTML_ISOLATED_FRAME'  # bool
ENV_OFFLINE = 'LETS_PLOT_OFFLINE'  # bool
ENV_DEV_OFFLINE = 'LETS_PLOT_DEV_OFFLINE'  # bool
ENV_MAX_WIDTH = 'LETS_PLOT_MAX_WIDTH'
ENV_MAX_HEIGHT = 'LETS_PLOT_MAX_HEIGHT'
ENV_MAPTILES_KIND = 'LETS_PLOT_MAPTILES_KIND'
ENV_MAPTILES_URL = 'LETS_PLOT_MAPTILES_URL'
ENV_MAPTILES_THEME = 'LETS_PLOT_MAPTILES_THEME'
ENV_GEOCODING_URL = 'LETS_PLOT_GEOCODING_URL'

HTML_ISOLATED_FRAME = 'html_isolated_frame'
MAX_WIDTH = 'max_width'
MAX_HEIGHT = 'max_height'


def _init_value(actual_name: str, def_val: Any) -> Any:
    env_val = _get_env_val(actual_name)
    return env_val if env_val else def_val


def _get_env_val(actual_name: str) -> Any:
    env_name = "LETS_PLOT_{}".format(actual_name.upper())
    return os.environ.get(env_name)


_settings = {
    'offline': _init_value('offline', False),  # default: download from CDN
    'js_base_url': 'https://dl.bintray.com/jetbrains/lets-plot',
    'js_name': '',  # default: lets-plot-<version>.min.js
    'geocoding_url': _init_value('geocoding_url', 'https://geo.datalore.jetbrains.com'),

    'dev_offline': _init_value('dev_offline', True),  # default: embed js into the notebook
    'dev_js_base_url': "http://0.0.0.0:8080",
    'dev_js_name': '',  # default: lets-plot-<version>.js
    'dev_geocoding_url': _init_value('dev_geocoding_url', 'https://geo.datalore.jetbrains.com')
}


def _to_actual_name(name: str) -> str:
    if name.startswith("dev_"):
        return name

    return name if is_production() else 'dev_' + name


def _get_global_val_intern(actual_name: str) -> Any:
    # `settings` dict has precedence over environment variables.
    env_val = _get_env_val(actual_name)
    return _settings.get(actual_name, env_val)


def is_production() -> bool:
    return 'dev' not in __version__


def has_global_value(name: str) -> bool:
    val = _get_global_val_intern(_to_actual_name(name))

    if isinstance(val, bool):
        return True
    if isinstance(val, str) and not val.strip():
        return False
    return bool(val)


def get_global_val(name: str) -> Any:
    if not has_global_value(name):
        raise ValueError("Not defined '{}'".format(_to_actual_name(name)))

    return _get_global_val_intern(_to_actual_name(name))


def get_global_str(name: str) -> str:
    val = get_global_val(name)
    if not isinstance(val, str):
        raise ValueError("Not string value: ['{}'] : {}".format(_to_actual_name(name), type(val)))
    return val


def get_global_bool(name: str) -> bool:
    val = get_global_val(name)
    if isinstance(val, bool):
        return val

    if isinstance(val, str):
        if val.lower() in ['true', '1', 't', 'y', 'yes']:
            return True
        elif val.lower() in ['false', '0', 'f', 'n', 'no']:
            return False
        else:
            raise ValueError("Can't convert str to boolean : ['{}'] : {}".format(_to_actual_name(name), val))

    raise ValueError("Not boolean value: ['{}'] : {}".format(_to_actual_name(name), type(val)))
